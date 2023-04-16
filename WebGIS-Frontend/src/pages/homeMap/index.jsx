import React, { memo, useEffect, useState } from 'react'
import { connect } from 'react-redux'
import Taro, { useReady } from '@tarojs/taro'
import { View, Text, Map, CoverView, CoverImage } from '@tarojs/components'

// 网络请求
import {
  reviewImages,
  reviewThumbnail
} from '../../api/index'

// 导入action
import {
  obtainCurrentMobileScreenPhoto
} from './store/action'

// 导入方法函数
import { transformFromGCJToWGS, transformFromWGSToGCJ } from '../../utils/coordinateTransformation'

import triangle from '../../assets/img/triangleBig.png'
import dw from '../../assets/img/dwb.png'
import layer from '../../assets/img/layer.png'
import './index.css'

const HomeMap = memo(props => {
  // 保存地图实例
  const [location, setLocation] = useState(null)

  // 保存当前经纬度
  const [longitude, setLongitude] = useState(0)
  const [latitude, setLatitude] = useState(0)

  // 保存当前地图数据
  const [onScreenData, setOnScreenData] = useState(null)

  // 保存左下右上坐标
  const [coordinate, setCoordinate] = useState(null)

  // 当前地图上的标注点
  const [customCallout, setCustomCallout] = useState(null)

  // 切换底图
  const [showSatelliteMap, setShowSatelliteMap] = useState(false)

// 获取当前位置
useEffect(() => {
  //-----------编码练习部分·开始---------------
  Taro.getLocation({
    success: res => {
      setLongitude(res.longitude)
      setLatitude(res.latitude)
    }
  })
  //-----------编码练习部分·结束---------------
}, [])

  // 页面渲染完成时触发
  useReady(() => {
    // 获取地图实例对象
    const MapContext = Taro.createMapContext('mapid')
    MapContext.moveToLocation()
    setLocation(MapContext)
  })

  // 右上左下坐标发生变化时触发
  useEffect(() => {
    if (coordinate) {
      // 右上左下经纬度
      const [UpperRight, LowerLeft] = coordinate
      // 赋值成字符串类型
      const UpperRightCoordinate = `${UpperRight.longitude},${UpperRight.latitude}`;
      const LowerLeftCoordinate = `${LowerLeft.longitude},${LowerLeft.latitude}`
      // 派发获取手机屏幕区域内照片的action
      props.requestOnScreenData(UpperRightCoordinate, LowerLeftCoordinate)
    }
  }, [coordinate])

  // 实时位置监听
  const _locationChangeFn = res => {
    const { longitude, latitude } = transformFromGCJToWGS(res.latitude, res.longitude)
    console.log(longitude, latitude);
    setLongitude(longitude)
    setLatitude(latitude)
  }

  // 地图发生变化时触发
  const regionChange = (e) => {
    //-----------编码练习部分·开始---------------
    const { type } = e
  if (type == 'end') {
    const { detail: { region } } = e
    const { northeast, southwest } = region
    const northeastTransformToWGS = transformFromGCJToWGS(northeast.latitude, northeast.longitude)
    const southwestTransformToWGS = transformFromGCJToWGS(southwest.latitude, southwest.longitude)
    setCoordinate([{ ...northeastTransformToWGS }, { ...southwestTransformToWGS }])
  }
    //-----------编码练习部分·结束---------------
  }

  // 当前手机屏幕内的照片数据
  useEffect(() => {
    if (props.HomeData) {
      const CustomCallout = props.HomeData.data.map((item, index) => {
        const { latitude, longitude } = transformFromWGSToGCJ(item.latitude, item.longitude)
        item.latitude = latitude
        item.longitude = longitude
        return {
          id: index + 1,
          iconPath: triangle,
          width: 40,
          height: 40,
          latitude: item.latitude,
          longitude: item.longitude,
          customCallout: {
            anchorY: 14,
            anchorX: 0,
            display: 'ALWAYS',
          },
          pictureCount: item.pictureCount,
          firstPictureName: `${reviewThumbnail}?pictureName=${item.firstPictureName}`,
          boxExtent: item.boxExtent
        }
      })
      setCustomCallout(CustomCallout);
    }
  }, [props.HomeData])

  // 上传图片
  const jumpUploadFilesPage = () => {
    Taro.navigateTo({
      url: '/pages/upload/index'
    })
  }

  // 点击定位拉回地图中心
  const onClickLocation = () => {
    location.moveToLocation()
  }

  // 点击气泡跳转到查看图片页面
  const jumpViewPicturePage = e => {
    const { markerId } = e.detail
    Taro.navigateTo({
      url: `/pages/viewPicture/index?markerId=${markerId}`
    })
  }

  const switchMap = e =>{
    setShowSatelliteMap(!showSatelliteMap);
  }

  return (
    //-----------编码练习部分·开始---------------
    <Map
      id='mapid'
      setting={{}}
      markers={customCallout ? customCallout : {}}
      latitude={latitude}
      longitude={longitude}
      style={{ height: '100vh', width: '100vw' }}
      scale='18'
      showCompass
      showLocation
      enableRotate
      enable-satellite={showSatelliteMap}
      onRegionChange={e => regionChange(e)}
      onCalloutTap={e => jumpViewPicturePage(e)}
    >
      {/* 地图控件配置区域 */}
      {/* 弹窗层控件 */}
    <CoverView slot='callout'>
    {
      customCallout && customCallout.length > 0 && customCallout.map(item => (
      /** 自定义样式的 callout */
        <CoverView markerId={item.id} key={item.id} className='bubbleContent'>
          <CoverView className='bubble'>
            {item.firstPictureName && <CoverImage src={item.firstPictureName} key={item.id} />}
          </CoverView>
          <CoverView className='imgNum'>{Number(item.pictureCount) > 99 ? '···' : item.pictureCount}</CoverView>
        </CoverView>
      ))
    }
  </CoverView>
  {/* 底图切换控件 */}
  <CoverView className='layer' onClick={e => switchMap()}>
    <CoverImage src={layer} className='LayerImg'></CoverImage>
  </CoverView>
  {/* 当前定位控件 */}
  <CoverView className='location' onClick={e => onClickLocation()}>
    <CoverImage src={dw} className='locationImg'></CoverImage>
  </CoverView>
  {/* 上传图片控件 */}
  <CoverView className='upload' onClick={e => jumpUploadFilesPage()}>
    <CoverView>+</CoverView>
  </CoverView>
    </Map>
    //-----------编码练习部分·结束---------------
  )
})

const mapStateToProps = state => ({
  HomeData: state.HomeOnScreenData.HomeOnScreenData
})

const mapDispatchToProps = dispatch => ({
  requestOnScreenData: (UpperRightCoordinate, LowerLeftCoordinate) => {
    dispatch(obtainCurrentMobileScreenPhoto(UpperRightCoordinate, LowerLeftCoordinate))
  }
})

export default connect(mapStateToProps, mapDispatchToProps)(HomeMap)