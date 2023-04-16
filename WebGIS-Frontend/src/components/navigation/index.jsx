import React, { memo, useState } from 'react'
import Taro from '@tarojs/taro'
import { View, Text } from '@tarojs/components'
import './index.css'


const Navigation = memo(props => {
  // 返回上一页
  const returnPreviousPage = () => {
    Taro.navigateBack({
      delta: 1
    })
  }

  // 获取系统导航条信息
  const [systemInfo, setSystemInfo] = useState(Taro.getApp().$app.systemInfo)

  return (
    <View style={`height:${systemInfo.navBarHeight}px;`} className='navBar'>
      <View className='navBarContent'
        style={`height:${systemInfo.menuHeight}px; min-height:${systemInfo.menuHeight}px; line-height:${systemInfo.menuHeight}px; left:${systemInfo.menuRight + 10}px; bottom:${systemInfo.menuTop}px`}
      >
        <View className='return' onClick={e => returnPreviousPage()}></View>
        <Text className='title'>{`${props.area ? props.area.pictureCount : '--'}张照片`}</Text>
      </View>
    </View>
  )
})

export default Navigation