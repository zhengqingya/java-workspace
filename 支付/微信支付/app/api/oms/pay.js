import request from '@/utils/request'

const BASE_API = '/pay'

export default {
	createOrder(data) {
		return request({
			url: BASE_API + '/createOrder',
			method: 'post',
			data
		})
	},
	refund(data) {
		return request({
			url: BASE_API + '/refund',
			method: 'post',
			data
		})
	}
}