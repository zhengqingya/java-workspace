import request from "@/utils/request";

const BASE_API = "/api/file";

export default {
  time() {
    return request({
      url: BASE_API + "/time",
      method: "get",
    });
  },
  upload(data) {
    return request({
      url: BASE_API + "/upload",
      method: "post",
      data,
    });
  },
  preSignUrl({ fileMd5, partNumber }) {
    return request({
      url: BASE_API + `/${fileMd5}/${partNumber}`,
      method: "get",
    });
  },
  merge(fileMd5) {
    return request({
      url: BASE_API + `/merge/${fileMd5}`,
      method: "post",
    });
  },
};
