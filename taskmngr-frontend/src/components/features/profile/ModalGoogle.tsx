import React, { useState } from "react";

interface ModalGoogleProps {
  open: boolean;
  onClose: () => void;
  onLogin: (email: string, senha: string) => void;
}

const ModalGoogle: React.FC<ModalGoogleProps> = ({ open, onClose }) => {
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");

  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40">
      <div className="bg-white rounded-2xl shadow-lg p-6 w-full max-w-md flex flex-col items-center">
        <img
          src="/ModalGoogle.jpg"
          alt="Google Calendar"
          className="w-full max-w-xs mb-6 rounded-lg"
        />
        <h2 className="text-lg font-semibold mb-4 text-center">
          Conecte sua conta com o Google Calendar!
        </h2>
        <form
          onSubmit={e => {
            e.preventDefault();
            if (!email || !senha) {
              setErro("Preencha todos os campos.");
              return;
            }
            setErro("");
            // caso de algum erro na autenticacao/login e senha vai vir aqui
          }}
          className="w-full flex flex-col items-center gap-4"
        >
          <div className="w-full relative">
            <span className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400">
              <i className="fa-regular fa-envelope"></i>
            </span>
            <input
              type="email"
              placeholder="E-mail"
              className="w-full border border-gray-300 rounded-md px-10 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
              value={email}
              onChange={e => setEmail(e.target.value)}
              required
            />
          </div>
          <div className="w-full relative">
            <span className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400">
              <i className="fa-solid fa-lock"></i>
            </span>
            <input
              type="password"
              placeholder="Senha"
              className="w-full border border-gray-300 rounded-md px-10 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
              value={senha}
              onChange={e => setSenha(e.target.value)}
              required
            />
          </div>
          {erro && <div className="text-red-500 text-xs">{erro}</div>}
          <div className="flex gap-4 w-full mt-2">
            <button
              type="button"
              className="flex-1 bg-[#D1D5DC] text-gray-600 rounded-md px-3 py-2 font-semibold hover:bg-gray-400 transition"
              onClick={onClose}
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="flex-1 bg-[#155DFC] text-white rounded-md px-3 py-2 font-semibold hover:bg-blue-800 shadow transition"
            >
              Conectar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ModalGoogle;