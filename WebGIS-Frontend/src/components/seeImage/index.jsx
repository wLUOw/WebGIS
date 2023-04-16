import React, { memo, useEffect, useState } from 'react'
import Taro, { downloadFile } from '@tarojs/taro'
import { View, Image, Icon, Text, Textarea } from '@tarojs/components'

import './index.css'

// 网络请求
import {
  reviewImages,
  seePictureMessage
} from '../../api/index'

const SeeImage = memo(props => {
  // 获取系统信息
  const [systemInfo, setSystemInfo] = useState(Taro.getApp().$app.systemInfo)
  // 保存需要查看的图片信息
  const [currentImages, setCurrentImg] = useState(props.setCurrentImg)
  const [clickIndex, setClickIndex] = useState(props.clickIndex)
  const [needCompress, setNeedCompress] = useState(props.needCompress)
  // 附言组件显示隐藏
  const [footerStatus, setFooterStatus] = useState(true)
  // 附言信息
  const [pictureMessage, setPictureMessage] = useState('')

  // 发送附言请求
  useEffect(() => {
    seePictureMessage({ pictureName: currentImages[clickIndex] }).then(res => setPictureMessage(res.data)).catch(err => console.log(err))
  }, [])

  // 控制附言组件
  const changeFooter = () => {
    setFooterStatus(!footerStatus)
  }

  // 阻止事件冒泡
  const stopDefaultEvent = e => {
    e.stopPropagation()
  }

  // 查看原图
  const viewOriginal = () => {
    //-----------编码练习部分·开始---------------
    if (needCompress === 0) {
      Taro.showToast({
        title: '该图已是原图，请勿重复切换',
        icon: 'none',
        duration: 2000,
        mask: true
      })
    } else {
      setNeedCompress(0)
    }
    //-----------编码练习部分·结束---------------
  }

  // 下载图片
  const download = () => {
    //-----------编码练习部分·开始---------------
    Taro.showToast({
      title: '长按保存图片',
      icon: 'none',
      duration: 2000,
      mask: true
    })
    return
    Taro.authorize({
      scope: 'scope.writePhotosAlbum',
      success: res => {
        Taro.saveImageToPhotosAlbum({
          filePath: `${reviewImages}?pictureName=${currentImages[clickIndex]}&needCompress=${needCompress}`,
          success: res => console.log(res),
          fail: err => console.log(err)
        })
      },
      fail: err => { }
    })
    //-----------编码练习部分·结束---------------
  }

  return (
    <View
      className='seeImgContent'
      style={`padding-top:${systemInfo.navBarHeight}px;height: calc(100vh - ${systemInfo.navBarHeight}px);`}
      onClick={e => changeFooter()}
    >
      <View className='pictureCount' style={`top:${systemInfo.navBarHeight + 10}px;left:50%;`}>{`${clickIndex + 1}/${currentImages.length}`}</View>
      <Icon type='cancel' color='#999' size='30' className='cancelIcon' style={`top:${systemInfo.navBarHeight + 10}px;right:12px`} onClick={e => props.changeComponents()} />
      <View className='showImage'
      >
        <Image src={`${reviewImages}?pictureName=${currentImages[clickIndex]}&needCompress=${needCompress}`} mode='widthFix' showMenuByLongpress />
      </View>
      {
        footerStatus &&
        <View className='footer' onClick={e => stopDefaultEvent(e)}>
          <Text className='pictureMessage'>
            {pictureMessage ? pictureMessage : '暂无留言-.-'}
          </Text>
          <View className='operation'>
            <View className='viewOriginal' onClick={e => viewOriginal()}>查看原图</View>
            <View className='download' onClick={e => download()}>下载</View>
          </View>
        </View>
      }
    </View >
  )
})

export default SeeImage