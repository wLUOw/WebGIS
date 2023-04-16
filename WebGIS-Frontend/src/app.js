import { Component } from 'react'
import Taro from '@tarojs/taro'

import { Provider } from 'react-redux'
import store from './store'

import './app.css'

class App extends Component {
  constructor(props) {
    super(props)
    this.systemInfo = {}
  }
  onLaunch() {
    // 获取系统信息，拿到状态栏高度
    const getSystemInfo = Taro.getSystemInfoSync()
    // 获取胶囊按钮位置信息
    const getMenuInfo = Taro.getMenuButtonBoundingClientRect()
    const globalNavHeight = {
      screenWidth: getSystemInfo.screenWidth,
      navBarHeight: (getMenuInfo.top - getSystemInfo.statusBarHeight) * 2 + getMenuInfo.height + getSystemInfo.statusBarHeight,
      menuRight: getSystemInfo.screenWidth - getMenuInfo.right,
      menuTop: getMenuInfo.top - getSystemInfo.statusBarHeight,
      menuHeight: getMenuInfo.height,
      platform: getSystemInfo.platform,
      screenHeight: getSystemInfo.screenHeight
    }
    Object.assign(this.systemInfo, globalNavHeight)
  }

  componentDidMount() { }

  componentDidShow() { }

  componentDidHide() { }

  componentDidCatchError() { }

  // this.props.children 是将要会渲染的页面
  render() {
    return (
      <Provider store={store}>
        {this.props.children}
      </Provider>
    )
  }
}

export default App
