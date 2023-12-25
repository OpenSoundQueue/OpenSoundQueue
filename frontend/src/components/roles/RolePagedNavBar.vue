<template>
  <nav>
    <div class="nav-element back" @click="goBack()">
      <img src="@/assets/icons/arrows/arrow_back.svg" :alt="$translate('altTexts.arrowBack')"/>
    </div>
    <div class="nav-element">
      <img src="@/assets/icons/role.svg" :alt="$translate('altTexts.role')"/>
      <div class="role-name">{{ roleName }}</div>
    </div>
    <div class="nav-element save-button">
      <!-- TODO: Style title -->
      <img v-show="store.roleEdited" class="undo" src="@/assets/icons/undo.svg"
           :alt="translate('altTexts.undo')"
           :title="translate('roleEdit.rollback')"
           @click="store.rollback()"/>
      <DynamicButton v-show="store.roleEdited" b-style="save" status="active" @click="store.save()">{{
          translate('roleEdit.save')
        }}
      </DynamicButton>
    </div>
  </nav>
</template>

<script setup lang="ts">
import {useRoleStore} from "@/stores/Role";
import {onMounted, ref} from "vue";
import type {Ref} from "vue";
import {storeToRefs} from "pinia";
import {translate} from "@/plugins/TranslationPlugin";
import DynamicButton from "@/components/buttons/DynamicButton.vue";
import {PopUpService} from "@/services/PopUpService";

const store = useRoleStore();
const roleName: Ref<string> = ref("New Role");

onMounted(() => {
  const refStore = storeToRefs(store);

  if (refStore.fetchedRole.value !== undefined) {
    roleName.value = refStore.fetchedRole.value?.name
  }
})

async function goBack() {
  if (store.roleEdited) {
    PopUpService.openPopUp(translate('popUp.editRole.unsavedChanges'), translate('popUp.editRole.save'));
    const userAction = await PopUpService.waitForUserAction();

    if (userAction === "accepted") {
      await store.save();
    }
  }
  emit('back')
}

const emit = defineEmits<{
  back: []
}>()
</script>

<style scoped>
nav {
  width: 100%;
  display: flex;
  height: 40px;
  justify-content: space-between;
  align-items: center;
  box-sizing: border-box;
}

.nav-element {
  display: flex;
  align-items: center;
  gap: 5px;
  width: 100%;
}

.nav-element:nth-child(2) {
  max-width: calc(100vw - 200px);
  justify-content: center;
}

.nav-element:first-child,.nav-element:last-child {
  width: 100px;
}

.role-name {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.nav-element img {
  height: 25px;
}

.nav-element.back:hover {
  cursor: pointer;
}

.save-button{
  justify-content: end;
}

.undo {
  margin: auto 0 auto 0;
}
</style>