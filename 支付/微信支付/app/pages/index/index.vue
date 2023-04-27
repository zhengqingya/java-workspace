<template>
	<view>
		<button @click="createOrder">下单</button>
		<button @click="refund">退款</button>
	</view>
</template>


<script setup>
	import {
		ref,
		toRefs,
		reactive,
		getCurrentInstance,
		onMounted
	} from 'vue';
	import {
		onLoad
	} from "@dcloudio/uni-app"
	// 组件实例
	const {
		proxy
	} = getCurrentInstance();

	const state = reactive({
		openId: "xxx", // TODO
		orderDesc: "测试商品",
		orderNo: 113,
		tenantId: 1,
		totalPrice: 1,
		tradeType: "JSAPI"
	});

	async function createOrder() {
		let payParams = await proxy.$api.pay.createOrder(state)

		// 使用参考 https://uniapp.dcloud.net.cn/api/plugins/payment.html
		uni.requestPayment({
			provider: 'wxpay',
			timeStamp: payParams.timeStamp,
			nonceStr: payParams.nonceStr,
			package: payParams.wxPackage,
			signType: payParams.signType,
			paySign: payParams.paySign,
			success: function(res) {
				console.log('success: ' + JSON.stringify(res));
			},
			fail: function(err) {
				console.log('fail: ' + JSON.stringify(err));
			}
		});
	};
	async function refund() {
		let result = await proxy.$api.pay.refund({
			orderNo: state.orderNo,
			refundDesc: "其它原因",
			refundOrderNo: 113,
			refundPrice: state.totalPrice,
			tenantId: state.tenantId,
			totalPrice: state.totalPrice
		})
		console.log(result)
	}
</script>

<style lang="scss" scoped>

</style>