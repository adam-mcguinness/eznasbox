import { defineStore } from 'pinia';

export const useNfsStore = defineStore('nfsStore', {
    state: () => ({
        nfsShares: [],
        error: null,  // Error state
        isLoading: false  // Loading state
    }),

    actions: {
        async getNfsShares() {
            try {
                const response = await fetch('http://localhost:8080/nfs');
                if (response.ok) {
                    this.nfsShares = await response.json();  // Assumes the server response is JSON
                } else {
                    throw new Error('Failed to fetch NFS shares');
                }
            } catch (error) {
                this.error = error.message;  // Storing the error message in the store's state
            }
        },
        async deleteNfsShare(index) {
            const apiUrl = `http://localhost:8080/nfs/${index}`;
            try {
                const response = await fetch(apiUrl, {method: 'DELETE'});
                if (!response.ok) {
                    throw new Error(response.statusText);
                }
                this.nfsShares = this.nfsShares.filter(item => item.index !== index);
            } catch (e){
                this.error = e
            }
        },
        async updateNfsShare(index, share) {
            const apiUrl = `http://localhost:8080/nfs`;
            const updatedShare = {
                index: index,
                ...share
            }
            try {
                const response = await fetch(apiUrl, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(updatedShare)
                });
                if (!response.ok) {
                    throw new Error(response.statusText);
                }
                await this.getNfsShares()
            } catch (e) {
                this.error = e
            }
        },
        async addNfsShare(share){
            const apiUrl = `http://localhost:8080/nfs`;
            try {
                const response = await fetch(apiUrl, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(share)
                });
                if (!response.ok) {
                    throw new Error(response.statusText);
                }
                await this.getNfsShares()
            } catch (e){
                this.error = e
            }
        }
    }
});
