<template>
  <span>666</span>
</template>

<script>
export default {
  methods: {
    data() {
      return {
        fileList: [],
      };
    },
    created() {
      console.log(111);
      this.test();
    },
    async test() {
      let res = await this.$api.file.time();
      console.log(res.data);
    },
  },
};
</script>

<style scoped></style>
