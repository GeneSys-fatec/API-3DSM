
export const getFileIcon = (mimeType: string) => {
    if (mimeType.includes('image')) return <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-purple-500" viewBox="0 0 20 20" fill="currentColor"><path fillRule="evenodd" d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 12H4l4.586-4.586a2 2 0 012.828 0L16 15zm-2-6a2 2 0 100-4 2 2 0 000 4z" clipRule="evenodd" /></svg>;
    if (mimeType.includes('pdf')) return <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-red-600" viewBox="0 0 20 20" fill="currentColor"><path d="M5.5 13a1.5 1.5 0 01-1.5-1.5v-2a1.5 1.5 0 013 0v2a1.5 1.5 0 01-1.5 1.5zm1.336-.5a2.5 2.5 0 10-3.356 3.356l1.242 1.242a.25.25 0 00.354 0l1.242-1.242A2.5 2.5 0 006.836 12.5zm.707-8.707a1 1 0 00-1.414 0L3.5 6.5l1.414 1.414L6 6.414V11a1 1 0 102 0V6.414l1.086 1.086 1.414-1.414-2.828-2.828z" /></svg>;
    if (mimeType.includes('document')) return <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-blue-500" viewBox="0 0 20 20" fill="currentColor"><path fillRule="evenodd" d="M4 4a2 2 0 012-2h8a2 2 0 012 2v12a2 2 0 01-2 2H6a2 2 0 01-2-2V4zm2 2a1 1 0 00-1 1v1a1 1 0 001 1h8a1 1 0 001-1V7a1 1 0 00-1-1H6zm-1 5a1 1 0 011-1h8a1 1 0 110 2H6a1 1 0 01-1-1zm1 3a1 1 0 100 2h6a1 1 0 100-2H6z" clipRule="evenodd" /></svg>;
    if (mimeType.includes('spreadsheet')) return <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-green-600" viewBox="0 0 20 20" fill="currentColor"><path fillRule="evenodd" d="M4 4a2 2 0 012-2h8a2 2 0 012 2v12a2 2 0 01-2 2H6a2 2 0 01-2-2V4zm2 2a1 1 0 00-1 1v1a1 1 0 001 1h8a1 1 0 001-1V7a1 1 0 00-1-1H6zm-1 5a1 1 0 011-1h8a1 1 0 110 2H6a1 1 0 01-1-1zm1 3a1 1 0 100 2h6a1 1 0 100-2H6z" clipRule="evenodd" /></svg>;
    return <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-gray-500" viewBox="0 0 20 20" fill="currentColor"><path fillRule="evenodd" d="M10 2a1 1 0 00-1 1v1h2V3a1 1 0 00-1-1zm-4 4a1 1 0 000 2h8a1 1 0 100-2H6zm-1 4a1 1 0 112 0 1 1 0 01-2 0zm5 0a1 1 0 112 0 1 1 0 01-2 0zm5 0a1 1 0 112 0 1 1 0 01-2 0zM4 14a1 1 0 00-1 1v1a1 1 0 001 1h12a1 1 0 001-1v-1a1 1 0 00-1-1H4z" clipRule="evenodd" /></svg>;
};

export const formatFileSize = (bytes: number) => {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};