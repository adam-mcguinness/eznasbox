<script setup lang="ts">
import {useSmbStore} from "~/store/smbStore";

const smbStore = useSmbStore()


onMounted(async () => {
  await smbStore.getSmbShares();
});

// Access the nfsExports from the store
const smbShares = computed(() => smbStore.smbShares);
</script>

<template>
  <v-data-table
      :items="smbShares"
  >
  </v-data-table>

</template>

<style scoped>

</style>