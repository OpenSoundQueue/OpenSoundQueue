<template>
  <div class="detail-container">
    <div class="detail-header">
      <p>{{ user ? user.username : $translate('adminPage.detail.placeholder.name') }}</p>
      <div class="dashed-hr"></div>
    </div>
    <div class="detail-body">
      <div class="email info-container">
        <div class="label">
          <img src="@/assets/icons/mail.svg"/>
          <p>{{ $translate('adminPage.tableHeader.email') }}:</p>
        </div>
        <p>{{ user ? user.email ? user.email : $translate('adminPage.detail.placeholder.email') : '' }}</p>
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
      <DynamicButton class="delete" b-style="login" :status="user?'valid':'invalid'">
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

const props = defineProps<{
  user: User
}>()

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

.info-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 20px;
}

.info-container > p,
.label > p {
  margin: 0;
  padding: 0;
}

.info-container > p {
  margin-left: 24px;
}

p {
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