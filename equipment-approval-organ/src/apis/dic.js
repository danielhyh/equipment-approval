import instance from "@/utils/request.js";

export function getDict() {
  return instance({
    url: `/admin-api/system/dict-data/simple-list`,
    method: "get",
  });
}
