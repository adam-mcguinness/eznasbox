<script setup lang="ts">
import { ref, reactive, watch, onMounted, defineProps, defineEmits } from 'vue';
import { useNfsStore } from "~/store/nfsStore";

const nfsStore = useNfsStore();
const props = defineProps({
  modelValue: Boolean,
  index: Number
});

const emits = defineEmits(['update:modelValue']);

const dialogOpen = ref(false);
const localItem = reactive({
  directory: '',
  client: '',
  options: []
});
const addOrUpdate = computed(() => {
  if (props.index == null) {
    return 'Add';
  } else {
    return 'Update';
  }
});

// Watch for modelValue and index changes
watch(() => props.modelValue, (newVal) => {
  dialogOpen.value = newVal;
  if (newVal) {
    if (props.index == null) {
      // Reset localItem if there's no index provided
      resetLocalItem();
    } else if (nfsStore.nfsShares[props.index]) {
      // Load existing data if index is provided
      const item = nfsStore.nfsShares[props.index];
      localItem.directory = item.directory;
      localItem.client = item.client;
      localItem.options = [...item.options];  // Make sure to copy the array
    }
  }
});

function resetLocalItem() {
  localItem.directory = '';
  localItem.client = '';
  localItem.options = [];
}

function closeDialog() {
  emits('update:modelValue', false);
}

function updateItem(){
  if (props.index === undefined) {
    nfsStore.addNfsShare(localItem);
  }else {
    nfsStore.updateNfsShare(props.index, localItem);
  }
    closeDialog();
}
</script>

<template>
  <v-dialog v-model="dialogOpen" max-width="500px">
    <v-card>
      <v-card-title class="headline">Edit Item</v-card-title>
      <v-card-text>
        <v-form>
          <v-text-field label="Directory" v-model="localItem.directory"></v-text-field>
          <v-text-field label="Client" v-model="localItem.client"></v-text-field>
          <v-combobox
              v-model="localItem.options"
              clearable
              chips
              multiple
              label="Combobox"
              :items="['RW', 'RO','SYNC', 'ASYNC', 'NO_ROOT_SQUASH', 'ROOT_SQUASH', 'ALL_SQUASH']"
              variant="solo-filled"
          ></v-combobox>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="green darken-1" @click="closeDialog">Close</v-btn>
        <v-btn color="green darken-1" @click="updateItem">{{ addOrUpdate }}</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<style scoped>

</style>