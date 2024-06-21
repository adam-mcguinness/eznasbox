<script setup lang="ts">
import { ref, watch, defineProps, defineEmits } from 'vue';
import {useNfsStore} from "~/store/nfsStore";

const nfsStore = useNfsStore()

const props = defineProps({
  modelValue: Boolean,
  index: Number
});

const emits = defineEmits(['update:modelValue']);

const dialogOpen = ref(props.modelValue);

watch(() => props.modelValue, (newVal) => {
  dialogOpen.value = newVal;
});

function closeDialog() {
  emits('update:modelValue', false);
}

function deleteItem(){
  nfsStore.deleteNfsShare(props.index)
  closeDialog()
}
</script>

<template>
  <v-dialog v-model="dialogOpen" max-width="500px">
    <v-card>
      <v-card-title class="headline">Delete Item?</v-card-title>
      <v-card-text>
        Are you sure you want to Delete?
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn @click="deleteItem">Okay</v-btn>
        <v-btn @click="closeDialog">Close</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<style scoped>

</style>