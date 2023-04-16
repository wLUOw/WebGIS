import React, { memo, useEffect, useState } from 'react'
import { connect } from 'react-redux'
import Taro, { useRouter } from '@tarojs/taro'
import { View, Image } from '@tarojs/components'

// 网络请求api
import {
  seeWithinAnAreaPhotos,
  reviewImages
} from '../../api/index'

// 样式
import './index.css'

// 引入导航组件
import Navigation from '../../components/navigation'
import SeeImage from '../../components/seeImage'

const ViewPicture = memo(props => {
  // 获取屏幕宽度
  const [screenWidth, setScreenWidth] = useState(Taro.getApp().$app.systemInfo.screenWidth)
  // 存储传递过来的markerId值
  const [router, setRouter] = useState(useRouter().params.markerId)
  // 点击区域的数据
  const [area, setArea] = useState(null)
  // 当前区域的照片数据
  const [currentImg, setCurrentImg] = useState(null)
  // 加载的图片数量
  const [imageOnLoadNumber, setImageOnLoadNumber] = useState(0)
  // 切换查看图片组件
  const [seeImageStatus, setSeeImageStatus] = useState(false)
  // 点击的图片索引
  const [clickIndex, setClickIndex] = useState()
  // 照片是否压缩
  const [needCompress, setNeedCompress] = useState(1)

  // 网络请求useEffect
  useEffect(async () => {
    // Taro.showLoading({
    //   title: '加载中...'
    // })
    try {
      // 根据传递过来的markerId值从redux里面获取对应区域的数据
      const currentArea = props.HomeDate.data[Number(router) - 1]
      const res = await seeWithinAnAreaPhotos({ boxExtent: currentArea.boxExtent })
      setArea(currentArea)
      setCurrentImg([...res.data])
    } catch (error) {
      console.log(error);
    }
  }, [])

  // 图片加载load
  const imageOnLad = num => {
    if (Number(imageOnLoadNumber) !== currentImg.length) {
      setImageOnLoadNumber(prevCount => prevCount + num)
    }
  }

  // 加载数量到达之后
  useEffect(() => {
    if (currentImg && Number(imageOnLoadNumber) === currentImg.length) {
      Taro.hideLoading()
    }
  }, [imageOnLoadNumber])

  // 点击查看图片
  const clickSeeImage = index => {
    setClickIndex(index)
    setSeeImageStatus(!seeImageStatus)
  }

  // 查看原图
  const seeArtist = () => {
    setNeedCompress(0)
  }

  // 切换
  const changeComponents = () => {
    setSeeImageStatus(!seeImageStatus)
  }

  return (
    <View>
      {
        seeImageStatus
          ?
          <SeeImage setCurrentImg={currentImg} clickIndex={clickIndex} seeArtist={seeArtist} needCompress={needCompress} changeComponents={changeComponents} />
          :
          <View className='content'>
            <Navigation area={area} />
            <View className='imgContent'>
              {
                currentImg && currentImg.map((item, index) => {
                  item = `${reviewImages}?pictureName=${item}&needCompress=${needCompress}`
                  return (
                    <View
                      className='item'
                      key={item}
                      style={`width:${(screenWidth / 4) - 2}px; height:${(screenWidth / 4) - 2}px;`}
                      onClick={e => clickSeeImage(index)}
                    >
                      {item && <Image src={item} style={`width:100%;`} />}
                    </View>
                  )
                })
              }
            </View>
          </View>
      }
    </View>
  )
})

const mapStateToProps = state => ({
  HomeDate: state.HomeOnScreenData.HomeOnScreenData
})

export default connect(mapStateToProps)(ViewPicture)