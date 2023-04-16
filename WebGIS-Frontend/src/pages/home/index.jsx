import React, { memo, useEffect, useState } from 'react'
import Taro, { useDidShow } from '@tarojs/taro'
import { View, Image, Text } from '@tarojs/components'

import dwLoading from '../../assets/img/dwLoding.png'

import './index.css'

const Home = memo(props => {
  // 授权状态
  const [locationAuthorize, setLocationAuthorize] = useState('')
  // 系统定位
  const [locationEnabled, setLocationEnabled] = useState('')
  // 轮询对象
  const [loop, setLoop] = useState(null)

  // 进入页面后，获取WebGIS小程序需要的权限：空间定位（用于定位用户当前位置）
  useEffect(() => {
    Taro.authorize({
      scope: 'scope.userLocation',
      success: (res) => { setLocationAuthorize(true) },
      fail: (res) => { setLocationAuthorize(false) },
      complete: () => { }
    })
    setLocationEnabled(Taro.getSystemInfoSync().locationEnabled)
  }, [])
  // 权限申请
  const setTheLocation = () => {
    //-----------编码练习部分·开始---------------
    Taro.getSetting({
      success: res => {
        //如果当前已经获取过，则转到首页
        if (locationAuthorize && locationEnabled) {
          jumpHome()
        } else {
          if (!res.authSetting['scope.userLocation']) {
            Taro.showModal({
              content: '检测到您未开启微信位置授权，是否前往开启',
              confirmText: '前往开启',
              success: res => {
                if (res.confirm) {
                  Taro.openSetting({
                    success: res => {
                      setLocationAuthorize(res.authSetting['scope.userLocation'])
                    },
                    fail: err => { },
                    complete: () => { }
                  })
                }
              }
            })
          } else {
            Taro.showModal({
              content: '定位失败，请检查您的网络或者手机系统定位是否开启',
              confirmText: '重新定位',
              success: res => {
                if (res.confirm) {
                  setLocationEnabled(Taro.getSystemInfoSync().locationEnabled)
                }
              },
              fail: err => { },
              complete: () => { }
            })
          }
        }
      },
      fail: err => { },
      complete: () => { }
    })
    //-----------编码练习部分·结束---------------
  }

  const jumpHome = () => {
    Taro.reLaunch({
      url: '/pages/homeMap/index'
    })
  }

  return (
    <View className='content'>
      {locationAuthorize === '' ?
        <View className='loading'>
          <View className='dwLoading'>
            <Image src={dwLoading} />
          </View>
          <Text>定位中...</Text>
        </View>
        :
        ((locationAuthorize && locationEnabled)
          ?
          jumpHome()
          :
          <View className='setUp'>
            <View className='setUpBackground'></View>
            <View className='tip'>检测到您未开启定位服务，暂时无法使用小程序</View>
            <View className='setUpBtn' onClick={e => setTheLocation()}>点击重试</View>
          </View>)
      }
    </View>
  )
})

export default Home