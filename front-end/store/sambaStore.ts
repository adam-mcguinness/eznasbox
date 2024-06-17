import { defineStore } from 'pinia';

export const useNfsStore = defineStore('nfsStore', {
    state: () => ({
        nfsExports: []
    }),

    actions: {
        async getNfsExports() {
            const apiUrl = "http://localhost:8080/nfs";
            try {
                const response = await fetch(apiUrl);
                if (!response.ok) {
                    throw new Error('Failed to fetch NFS exports');
                }
                this.nfsExports = await response.json();
            } catch (error) {
                console.error('Error fetching NFS exports:', error);
                // Optionally handle the error by updating the state or using a dedicated error state
            }
        }
    }
});