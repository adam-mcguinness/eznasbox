import { defineStore } from 'pinia';

export const useNfsStore = defineStore('nfsStore', {
    state: () => ({
        nfsShares: [],
        error: null,  // Error state
        isLoading: false  // Loading state
    }),

    actions: {
        async getNfsExports() {
            this.isLoading = true;  // Start loading
            const apiUrl = "http://localhost:8080/nfs";
            try {
                const response = await fetch(apiUrl);
                if (!response.ok) {
                    throw new Error(`Failed to fetch NFS exports: ${response.statusText}`);
                }
                this.nfsShares = await response.json();  // Correct the state property
                this.error = null;  // Clear errors on success
            } catch (error) {
                console.error('Error fetching NFS exports:', error);
                this.error = error.message;  // Update error state
            } finally {
                this.isLoading = false;  // Stop loading
            }
        }
    }
});
