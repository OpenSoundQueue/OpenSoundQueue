<template>
  <div class="detail-container">
    <div class="detail-header">
      <p v-if="!user">{{ $translate('adminPage.detail.placeholder.name') }}</p>
      <p v-else>{{ user.username }}</p>
      <div class="dashed-hr"></div>
    </div>
    <div v-if="!user" class="detail-body-empty">
      <p>{{ $translate('adminPage.detail.placeholder.body') }}</p>
    </div>
    <div v-else class="detail-body">
      <div class="email info-container">
        <div class="label">
          <img src="@/assets/icons/mail.svg"/>
          <p>{{ $translate('adminPage.tableHeader.email') }}:</p>
        </div>
        <p v-if="user?!user.email:false">{{ $translate('adminPage.detail.placeholder.email') }}</p>
        <a v-else :href="'mailto:'+(user ? user.email:'')">{{ user ? user.email:'' }}</a>
      </div>
      <div class="last-online info-container">
        <div class="label">
          <img src="@/assets/icons/time.svg"/>
          <p>{{ $translate('adminPage.tableHeader.lastOnline') }}:</p>
        </div>
        <p>{{ formattedTimestamp }}</p>
      </div>
      <div class="role info-container">
        <div class="label">
          <img src="@/assets/icons/role.svg"/>
          <p>{{ $translate('adminPage.tableHeader.role') }}</p>
        </div>
        <p><span class="dot"></span>{{ user ? user.role : '' }}</p>
      </div>
      <DynamicButton class="delete" b-style="login" :status="user?'valid':'invalid'" @click="deleteUser">
        <img src="@/assets/icons/delete.svg"/>
        {{ $translate('adminPage.detail.delete') }}
      </DynamicButton>
    </div>
  </div>
</template>

<script setup lang="ts">
import {User} from "@/models/User";
import {computed} from "vue";
import {translate} from "@/plugins/TranslationPlugin";
import DynamicButton from "@/components/buttons/DynamicButton.vue";
import {HttpService} from "@/services/HttpService";

const httpService = new HttpService();

const props = defineProps<{
  user: User | undefined | null
}>()

const emit = defineEmits<{
  "delete:User": [data: User | undefined | null]
}>();

const formattedTimestamp = computed(() => {
  if (!props.user) {
    return ""
  }

  const date = new Date(props.user.lastOnline);
  const day = date.getDate();
  const month = date.getMonth() + 1;
  const year = date.getFullYear();
  const hours = date.getHours();
  const minutes = date.getMinutes();
  const seconds = date.getSeconds();

  const formattedTime = `${hours}:${minutes}:${seconds}`;

  return `${day}. ${translate('months.' + (month))}, ${year}, ${formattedTime}`;
})

function deleteUser():void{
  if (props.user){
    httpService.deleteUser(props.user.id)
        .then(() => {
          emit("delete:User",props.user)
        })
  }

}
</script>

<style scoped>

.detail-container {
  height: 100%;
  border-radius: var(--border-radius-big);
  background: var(--secondary-color);
  display: flex;
  flex-direction: column;
}

.detail-header {
  display: flex;
  flex-direction: column;
  padding: 10px;
  justify-content: center;
  text-align: center;
}

.detail-body {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.detail-body-empty{
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.info-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin: 20px;
  width: calc(100% - 64px);
}

.info-container > p,
.label > p {
  margin: 0;
  padding: 0;
}

.info-container > p,a {
  margin-left: 24px;
}

p,a {
  color: white;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}


.dashed-hr {
  height: 0;
  border-top: dashed 2px var(--tertiary-color);
}

.role > p {
  font-size: var(--font-size-small);
  background-color: var(--background-color);
  width: fit-content;
  max-width: 100%;
  box-sizing: border-box;
  padding: 5px;
  border-radius: var(--border-radius-small);
  text-align: left;
}

.dot {
  height: 5px;
  aspect-ratio: 1/1;
  background-color: var(--tertiary-color);
  border-radius: 50%;
  display: inline-block;
  margin-right: 5px;
}

.label {
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
}

.delete {
  width: calc(100% - 40px);
  margin: auto auto 20px;
  font-weight: bold;
}

@media screen and (max-width: 1250px) {

}

</style>