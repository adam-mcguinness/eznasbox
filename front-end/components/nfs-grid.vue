<script setup lang="ts">
import {useNfsStore} from "~/store/nfsStore";
import UpdateNfsItem from "~/components/update-nfs-item.vue";
import ConfirmDelete from "~/components/confirmDelete.vue";

const nfsStore = useNfsStore()

const currentItem = ref({
  index: null,
  directory: '',
  client: '',
  options: '',
});

onMounted(async () => {
  await nfsStore.getNfsShares();
});

// Access the nfsExports from the store
const nfsShares = computed(() => nfsStore.nfsShares);

const headers = ref([
  {
    title: 'Directory',
    align: 'start',
    key: 'directory',
  },
  { title: 'Client', key: 'client' },
  { title: 'Options', key: 'options' },
  { title: 'Actions', key: 'actions', sortable: false },
])

const isEditDialogOpen = ref(false);
const isConfirmDialogOpen = ref(false);

function newItem() {
  console.log('Adding a new NFS share');
  // Open a dialog or component to add a new NFS share
}

function openConfirmDialog(item){
  currentItem.value = item;
  isConfirmDialogOpen.value = true;
}

function openEditDialog(item) {
  if (currentItem.index === null) {
    currentItem.index = nfsShares.value.length;
  }
  currentItem.value = item;
  isEditDialogOpen.value = true;
}
</script>

<template>
  <v-card>
    <v-toolbar flat>
      <v-toolbar-title>NFS Shares</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn color="primary" @click="openEditDialog">
        New NFS Share
      </v-btn>
    </v-toolbar>

    <v-data-table :headers="headers" :items="nfsShares">
      <template v-slot:item.actions="{ item }">
        <v-icon class="mr-2" @click="openEditDialog(item)">
          mdi-pencil
        </v-icon>
        <v-icon @click="openConfirmDialog(item)">
          mdi-delete
        </v-icon>
      </template>
    </v-data-table>

    <update-nfs-item v-model="isEditDialogOpen" :index="currentItem.index" />
    <confirm-delete v-model="isConfirmDialogOpen" :index="currentItem.index" />
  </v-card>
</template>

<style scoped>

</style>