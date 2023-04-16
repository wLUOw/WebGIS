import React, { memo, useState } from 'react'
import Taro from '@tarojs/taro'
import { Textarea, View, Image, Icon } from '@tarojs/components'

import './index.css'
// 网络请求
import {uploadImgUrl} from '../../api/index'

const Upload = memo(props => {
  // 选择的图片数量
  const [uploadImageNum, setImageNum] = useState(0)
  // 选择的图片路径
  const [uploadImagePath, setUploadImagePath] = useState(null)
  // 防抖变量
  const [timeouts, setTimeouts] = useState(null)
  // 附言
  const [value, setValue] = useState('')

  // 选择图片
  const uploadImage = () => {
    //-----------编码练习部分·开始---------------
    Taro.chooseImage({
      count: 9 - uploadImageNum,
      sizeType: ['original'],
      sourceType: ['album', 'camera'],
      success: res => {
        const filePaths = res.tempFilePaths;
        if (uploadImagePath) {
          setUploadImagePath(prevCount => [...prevCount, ...filePaths])
          setImageNum(prevCount => prevCount + filePaths.length)
        } else {
          setUploadImagePath(filePaths)
          setImageNum(prevCount => prevCount + filePaths.length)
        }
      }
    })
    //-----------编码练习部分·开始---------------
  }

  // 预览图片
  const previewImg = index => {
    Taro.previewImage({
      urls: uploadImagePath,
      current: uploadImagePath[index]
    })
  }

  // 删除图片
  const deleteImg = (e, index) => {
    e.stopPropagation()
    Taro.showModal({
      content: '确定要删除这张照片吗?',
      confirmText: '删除',
      confirmColor: '#11111',
      success: res => {
        if (res.confirm) {
          let newTempFilePaths = uploadImagePath
          uploadImagePath.splice(index, 1)
          setUploadImagePath(newTempFilePaths)
          setImageNum(uploadImageNum - 1)
        }
      }
    })
  }

  // 防抖函数
  const debounce = (fn, time, e) => {
    if (timeouts !== null) clearTimeout(timeouts)
    setTimeouts(setTimeout(fn(e), time))
  }

  // 输入事件
  function changeInput(e) {
    return function () {
      const { value } = e.detail
      setValue(value)
    }
  }

    // 点击提交照片与附言
    const submit = async () => {
      //-----------编码练习部分·开始---------------
      if (uploadImagePath && uploadImagePath.length !== 0) {
        Taro.showLoading({
          title: '提交中...'
        })
        // 对输入内容过滤空格符
        const filtrateSpaceValue = value.replace(/\s+/g, '')
        // 获取当前经纬度
        const { longitude, latitude } = await Taro.getLocation()
        const location = `${longitude},${latitude}`;
        // promise.all 统一上传多张图片，某一张失败，就走catch回调
        Promise.all(uploadImagePath.map(item => Taro.uploadFile({
          url: uploadImgUrl,
          name: 'file',
          filePath: item,
          formData: {
            attachment: filtrateSpaceValue,
            location
          },
          success: res => console.log(res),
          fail: err => console.log(err)
        })))
          .then(res => {
            Taro.hideLoading()
            Taro.showToast({
              title: '提交成功',
              icon: 'success',
              duration: 2000,
              mask: true,
              success: res => {
                setTimeout(() => {
                  Taro.navigateBack({
                    delta: 1
                  })
                }, 2000)
              }
            })
          })
          .catch(err => {
            Taro.hideLoading()
            Taro.showToast({
              title: '提交失败，请稍后重试',
              icon: 'none',
              duration: 2000,
              mask: true
            })
          })
      } else {
        Taro.showToast({
          title: '请选择要上传的图片',
          icon: 'none',
          duration: 2000,
          mask: true
        })
      }
      //-----------编码练习部分·结束---------------
    }

  return (
    <View className='content'>
      <View className='input'>
        <Textarea
          autoHeight
          placeholder='此刻的想法'
          disableDefaultPadding
          cursorSpacing={10}
          maxlength={200}
          onInput={e => debounce(changeInput, 300, e)}
        />
      </View>
      <View className='uploadFile'>
        {
          uploadImagePath && uploadImagePath.map((item, index) => {
            return (
              <View className='file' onClick={e => previewImg(index)}>
                <Image src={item}></Image>
                <Icon className='delete' type='cancel' size='18' color='#999' onClick={e => deleteImg(e, index)}></Icon>
              </View>
            )
          })
        }
        {
          (uploadImagePath && uploadImagePath.length >= 9)
            ?
            ''
            :
            <View className='upload' onClick={e => uploadImage()}></View>
        }
      </View>
      <View className='submit' onClick={e => submit()}>提 交</View>
    </View>
  )
})

export default Upload