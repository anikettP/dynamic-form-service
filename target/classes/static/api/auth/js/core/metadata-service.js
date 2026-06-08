// Reusable Metadata Service
class MetadataService {
    static cache = {};

    static async getFormMetadata(formName) {
        if (this.cache[formName]) {
            return this.cache[formName];
        }

        try {
            const data = await ApiClient.get(`/api/forms/${formName}`);
            // Cache the metadata response
            this.cache[formName] = data;
            return data;
        } catch (err) {
            console.error(`Error loading metadata for form '${formName}':`, err);
            throw err;
        }
    }

    static getFieldsSorted(metadata) {
        if (!metadata || !metadata.fields) return [];
        return [...metadata.fields].sort((a, b) => {
            const orderA = a.display_order || 0;
            const orderB = b.display_order || 0;
            return orderA - orderB;
        });
    }
}

window.MetadataService = MetadataService;
