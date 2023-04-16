import { http, HOST } from "../server/request";

// 查看图片
export const reviewImages = HOST + 'PictureInfo/viewPicture'

// 查看缩略图
export const reviewThumbnail = HOST + 'PictureInfo/viewThumbnailPicture'

// 上传图片
export const uploadImgUrl =  HOST + 'PictureInfo/uploadPicture'

// 获取当前手机屏幕区域内的照片
export const CurrentMobileScreenPhoto = (data) => http({ data, url: 'PictureInfo/picturesInAreaByBox', method: 'GET' })

// 查看某个区域的照片
export const seeWithinAnAreaPhotos = (data) => http({ data, url: 'PictureInfo/picturesByBox', method: 'GET' })

// 查看图片附言
export const seePictureMessage = (data) => http({ data, url: 'PictureInfo/viewAttachment', method: 'GET' })