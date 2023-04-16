import * as constant from './constant';

const HomeOnScreenData = {
  HomeOnScreenData: null
}

export function obtainHomeOnScreenData(state = HomeOnScreenData, action) {
  switch (action.type) {
    case constant.QUERY_CURRENT_MOBILE_SCREEN_IMAGE:
      return { ...state, HomeOnScreenData: action.onPhoneScreenData }
    default:
      return state
  }
}