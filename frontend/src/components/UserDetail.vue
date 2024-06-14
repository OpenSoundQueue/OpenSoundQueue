<template>
  <div class="detail-container">
    <div class="detail-header desktop">
      <p v-if="!user">{{ $translate('adminPage.detail.placeholder.name') }}</p>
      <p v-else>{{ user.username }}</p>
      <div class="dashed-hr"></div>
    </div>
    <div class="mobile">
      <div class="hr"></div>
      <DynamicButton v-if="user?user.role!=='Owner':false" class="delete" b-style="login" :status="buttonStatus"
                     @click="deleteUser">
        <img src="@/assets/icons/delete.svg" :alt="$translate('altTexts.delete')"/>
        {{ $translate('adminPage.detail.delete') }}
      </DynamicButton>
      <div class="detail-header" @click="toggleDropdown">
        <img src="@/assets/icons/user.svg" :alt="$translate('altTexts.user')"/>
        <p v-if="!user">{{ $translate('adminPage.detail.placeholder.name') }}</p>
        <p v-else>{{ user.username }}</p>
        <img class="arrow" src="@/assets/icons/arrows/keyboard_arrow_right.svg"
             :class="{ 'turn-down':detailVisibility  }" :alt="$translate('altTexts.arrowRight')"/>
      </div>
    </div>
    <div v-show="!user" class="detail-body-empty">
      <p>{{ $translate('adminPage.detail.placeholder.body') }}</p>
    </div>
    <div v-show="user" class="detail-body" :class="{ 'slide-out':!detailVisibility,'slide-in': detailVisibility  }">
      <div class="email info-container">
        <div class="label">
          <img src="@/assets/icons/mail.svg" :alt="$translate('altTexts.mail')"/>
          <p>{{ $translate('adminPage.tableHeader.email') }}:</p>
        </div>
        <p v-if="user?!user.email:false">{{ $translate('adminPage.detail.placeholder.email') }}</p>
        <a v-else :href="'mailto:'+(user ? user.email:'')">{{ user ? user.email : '' }}</a>
      </div>
      <div class="last-online info-container">
        <div class="label">
          <img src="@/assets/icons/time.svg" :alt="$translate('altTexts.time')"/>
          <p>{{ $translate('adminPage.tableHeader.lastOnline') }}:</p>
        </div>
        <p>{{ formattedTimestamp }}</p>
      </div>
      <div class="role info-container">
        <div class="label">
          <img src="@/assets/icons/role.svg" :alt="$translate('altTexts.role')"/>
          <p>{{ $translate('adminPage.tableHeader.role') }}</p>
        </div>
        <p><span class="dot"></span>{{ user ? user.role : '' }}</p>
      </div>
      <DynamicButton v-if="user?user.id != selfID:false" class="delete desktop" b-style="login" :status="buttonStatus"
                     @click="deleteUser">
        <img src="@/assets/icons/delete.svg" :alt="$translate('altTexts.delete')"/>
        {{ $translate('adminPage.detail.delete') }}
      </DynamicButton>
    </div>
  </div>
</template>

<script setup lang="ts">
import {User} from "@/models/User";
import {computed, ref} from "vue";
import {translate} from "@/plugins/TranslationPlugin";
import DynamicButton from "@/components/buttons/DynamicButton.vue";
import {HttpService} from "@/services/HttpService";
import {ToastService} from "@/services/ToastService";
import {PopUpService} from "@/services/PopUpService";

const httpService = new HttpService();

const props = defineProps<{
  user: User | undefined | null,
  selfID: number
}>()

const emit = defineEmits<{
  "update:Users": [data: User[]]
}>();
const detailVisibility = ref(false);

const popUpString = computed(() => {
  return `${translate('popUp.deleteUser.part1')} "${(props.user ? props.user.username : '')}" ${translate('popUp.deleteUser.part2')}`
})

const buttonStatus = computed(() => {
  if (!props.user) {
    return "inactive"
  }
  return "active"
})

const formattedTimestamp = computed(() => {
  if (!props.user) {
    return ""
  }

  if (!props.user.lastOnline) {
    return translate("adminPage.detail.placeholder.lastOnline")
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

async function deleteUser() {
  if (props.user) {

    PopUpService.openPopUp(popUpString.value, translate('popUp.deleteUser.button'));
    const userAction = await PopUpService.waitForUserAction();

    if (userAction === "accepted") {
      httpService.deleteUser(props.user.id)
          .then((response) => {
            ToastService.sendNotification(
                `${translate("notifications.userDelete.user")} "${props.user?.username}" ${translate("notifications.userDelete.messageSuccess")}`,
                "success",
                3000
            );
            emit("update:Users", response)
          }).catch(() => {
        ToastService.sendNotification(
            `${translate("notifications.userDelete.user")} "${props.user?.username}" ${translate("notifications.userDelete.messageError")}`,
            "error",
            3000
        );
      });
    }
  }

}

function toggleDropdown(): void {
  detailVisibility.value = !detailVisibility.value;
}
</script>

<style scoped>

.detail-container {
  height: 100%;
  border-radius: var(--border-radius-big);
  background: rgb(var(--secondary-color));
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

.detail-body-empty {
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

.info-container > p, a {
  margin-left: 34px;
}

p, a {
  color: white;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}


.dashed-hr {
  height: 0;
  border-top: dashed 2px rgb(var(--tertiary-color));
}

.role > p {
  font-size: var(--font-size-small);
  background-color: rgb(var(--background-color));
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
  background-color: rgb(var(--tertiary-color));
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

.mobile {
  display: none;
}

@media screen and (max-width: 1250px) {
  .detail-container {
    background-color: rgb(var(--background-color));
    height: fit-content;
  }

  .desktop {
    display: none;
  }

  .mobile {
    display: block;
  }

  .detail-header {
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    gap: 15px;
    width: 100%;
  }

  .detail-header > p {
    width: 100%;
    text-align: left;
  }

  .delete {
    width: 100%;
  }

  .hr {
    height: 10px;
    margin-top: 10px;
    border-top: dashed 3px rgb(var(--secondary-color));
  }

  .arrow {
    transition: transform 0.25s ease-in-out;
  }

  .turn-down {
    transform: rotate(90deg);
  }

  .detail-body {
    max-height: 0;
    overflow: hidden;
  }

  .mobile {
    border-bottom: solid 2px rgb(var(--secondary-color));
  }

  .slide-in {
    animation: slideIn 0.75s ease-in-out;
    max-height: 1000px;
  }

  .slide-out {
    animation: slideOut 0.75s ease-in-out;
  }

  @keyframes slideIn {
    from {
      max-height: 0;
    }
    to {
      max-height: 1000px;
    }
  }

  @keyframes slideOut {
    from {
      max-height: 1000px;
    }
    to {
      max-height: 0;
    }
  }
}

</style>