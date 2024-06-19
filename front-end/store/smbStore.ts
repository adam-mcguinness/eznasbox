import { defineStore } from 'pinia';

export const useSmbStore = defineStore('smbStore', {
    state: () => ({
        smbShares: [],
        error: null,  // Added to track errors
        isLoading: false  // Track loading state
    }),

    actions: {
        async getSmbShares() {
            this.isLoading = true;  // Indicate loading process has started
            const apiUrl = "http://localhost:8080/smb/shares"; // ensure the path is correct
            try {
                const response = await fetch(apiUrl);
                if (!response.ok) {
                    throw new Error(`Failed to fetch SMB shares: ${response.statusText}`);
                }
                this.smbShares = await response.json();
                this.error = null;  // Reset errors on successful fetch
            } catch (error) {
                console.error('Error fetching Smb Shares:', error);
                this.error = error.message;  // Update error state
            } finally {
                this.isLoading = false;  // Indicate loading process has ended
            }
        }
    }
});
