import React from "react";

interface ModalGoogleProps {
  open: boolean;
  onClose: () => void;
  onLoginCode?: (code: string) => void;
}

const CLIENT_ID = "472190232103-t2mfcfugbmg073901q238tspuv69bs9u.apps.googleusercontent.com"; 
const SCOPE = "https://www.googleapis.com/auth/calendar";

declare global {
  interface Window {
    google: any;
  }
}

const ModalGoogle: React.FC<ModalGoogleProps> = ({ open, onClose, onLoginCode }) => {
  if (!open) return null;

  const handleGoogleLogin = () => {
    if (!window.google) {
      alert("Google SDK não carregado.");
      return;
    }
    const client = window.google.accounts.oauth2.initCodeClient({
      client_id: CLIENT_ID,
      scope: SCOPE,
      ux_mode: "popup",
      callback: (response: { code: string }) => {
        if (response.code && onLoginCode) onLoginCode(response.code);
        onClose();
      },
    });
    client.requestCode();
  };

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-black/40"
      onClick={onClose} 
    >
      <div
        className="bg-white rounded-2xl shadow-lg p-8 w-full max-w-xl min-h-[520px] flex flex-col items-center relative"
        onClick={e => e.stopPropagation()} 
      >
        <div className="flex-1 w-full flex flex-col items-center">
          <img
            src="/ModalGoogle.jpg"
            alt="Google Calendar"
            className="w-full max-w-xs mb-6 rounded-lg"
          />
          <h2 className="text-lg font-semibold mb-4 text-center">
            Conecte sua conta com o Google Calendar!
          </h2>
        </div>
        <div className="flex w-full justify-between gap-8 mt-auto">
          <button
            type="button"
            className="w-1/2 bg-gray-200 text-gray-700 rounded-md px-4 py-3 font-semibold hover:bg-gray-400 transition"
            onClick={onClose}
          >
            Cancelar
          </button>
          <button
            type="button"
            className="w-1/2 flex items-center justify-center gap-2 bg-red-500 hover:bg-red-600 text-white font-semibold px-4 py-3 rounded-lg transition-colors shadow"
            onClick={handleGoogleLogin}
          >
            <i className="fa-brands fa-google"></i>
            Conectar com Google
          </button>
        </div>
      </div>
    </div>
  );
};

export default ModalGoogle;