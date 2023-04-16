export default {
  pages: [
    'pages/home/index',
    'pages/homeMap/index',
    'pages/upload/index',
    'pages/viewPicture/index',
  ],
  window: {
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#fff',
    navigationBarTextStyle: 'black'
  },
  requiredBackgroundModes: ['location'],
  "permission": {
    "scope.userLocation": {
      "desc": "您的位置信息将用于显示您位置周围的信息" // 高速公路行驶持续后台定位
    }
  }
}
