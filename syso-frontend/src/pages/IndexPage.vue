<template>
  <div class="index-page">
    <a-input-search
      v-model:value="searchParams.text"
      placeholder="请输入搜索关键词"
      enter-button="搜索"
      size="large"
      @search="onSearch"
    />
    <MyDivider />
    <a-tabs v-model:activeKey="activeKey" @change="onTabChange">
      <a-tab-pane key="post" tab="文章">
        <PostList :post-list="postList" />
      </a-tab-pane>
      <a-tab-pane key="picture" tab="图片">
        <PictureList :picture-list="pictureList" />
      </a-tab-pane>
      <a-tab-pane key="user" tab="用户">
        <UserList :user-list="userList" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts" setup>
import { ref, watchEffect } from "vue";
import PostList from "@/components/PostList.vue";
import MyDivider from "@/components/MyDivider.vue";
import PictureList from "@/components/PictureList.vue";
import UserList from "@/components/UserList.vue";
import { useRoute, useRouter } from "vue-router";
import { message } from "ant-design-vue";
import myAxios from "@/plugins/myAxios";
const postList = ref([]);

const userList = ref([]);

const pictureList = ref([]);

myAxios.post("post/list/page/vo", {}).then((res) => {
  postList.value = res.records;
});
myAxios.post("user/list/page/vo", {}).then((res: any) => {
  userList.value = res.records;
});
// myAxios.post("picture/list/page/vo", {}).then((res: any) => {
//   pictureList.value = res.records;
// });
const router = useRouter();
const route = useRoute();

const activeKey = route.params.category || "post";
const initSearchParams = {
  type: activeKey,
  text: "",
  pageSize: 10,
  pageNum: 1,
};

const searchText = ref(route.query.text || "");

/**
 * 加载数据
 * @param params
 */
const loadData = (params: any) => {
  const { type = "post" } = params;
  if (!type) {
    message.error("类别为空");
    return;
  }
};

const searchParams = ref(initSearchParams);

watchEffect(() => {
  searchParams.value = {
    ...initSearchParams,
    text: route.query.text,
    type: route.params.type,
  } as any;
  loadData(searchParams.value);
});

const onSearch = (value: string) => {
  alert(value);
  router.push({
    query: {
      ...searchParams.value,
      text: value,
    },
  });
};
const onTabChange = (key: string) => {
  router.push({
    path: `/${key}`,
    query: searchParams.value,
  });
};
</script>
