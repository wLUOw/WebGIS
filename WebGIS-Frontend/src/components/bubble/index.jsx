import React, { memo } from 'react'
import Taro from '@tarojs/taro'
import { View, CoverView } from '@tarojs/components'

import './index.css'

const Bubble = memo(props => {
  return (
    <CoverView className='content'>
      <CoverView className='bubble' style='overflow: visible;'>
        <CoverView className='test'><cover-view class="space" /></CoverView>
      </CoverView>
    </CoverView>
  )
})

export default Bubble