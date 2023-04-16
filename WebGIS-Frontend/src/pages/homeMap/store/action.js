import * as constant from './constant';

// 网络请求api
import { CurrentMobileScreenPhoto } from '../../../api';

// 保存手机屏幕内的照片数据
export const onPhoneScreenDataAction = onPhoneScreenData => ({
  type: constant.QUERY_CURRENT_MOBILE_SCREEN_IMAGE,
  onPhoneScreenData
})

// 获取手机屏幕区域内的照片
export const obtainCurrentMobileScreenPhoto = (UpperRightCoordinate, LowerLeftCoordinate) => {
  return dispatch => {
    const viewBoundary = `${UpperRightCoordinate},${LowerLeftCoordinate}`;
    CurrentMobileScreenPhoto({ viewBoundary }).then(res => {
      dispatch(onPhoneScreenDataAction(res))
    }).catch(err => console.log(err, '获取失败'))
  }
}