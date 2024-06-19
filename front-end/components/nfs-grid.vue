<script setup lang="ts">
import {useNfsStore} from "~/store/nfsStore";

const nfsStore = useNfsStore()


onMounted(async () => {
  await nfsStore.getNfsExports();
});

// Access the nfsExports from the store
const nfsExports = computed(() => nfsStore.nfsExports);
</script>

<template>
<v-data-table
:items="nfsExports"
>
</v-data-table>

</template>

<style scoped>

</style>