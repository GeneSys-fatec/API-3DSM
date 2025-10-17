import React from 'react';
import { AlertTriangle, MessageSquare, ClipboardList, MoreVertical } from 'lucide-react';
import { Notificacao } from '@/types/types';
import MenuNotificacao from './MenuNotificacao';


const IconeExpirado = () => (
  <div className="p-2 rounded-full bg-red-100 text-red-600">
    <AlertTriangle />
  </div>
);

const IconeComentario = () => (
  <div className="p-2 rounded-full bg-gray-100 text-gray-600">
    <MessageSquare />
  </div>
);

const IconDesignado = () => (
  <div className="p-2 rounded-full bg-blue-100 text-blue-600">
    <ClipboardList />
  </div>
);


interface CardNotificacaoProps {
  notificacao: Notificacao;
  onDelete: (id: string) => void;
}


const CardNotificacao: React.FC<CardNotificacaoProps> = ({ notificacao, onDelete }) => {

  const [menuAberto, setMenuAberto] = React.useState(false);

  const getIcon = () => {
    switch (notificacao.tipo) {
      case 'comentario':
        return <IconeComentario />;
      case 'atribuido':
        return <IconDesignado />;
      default:
        return <IconeExpirado />;
    }
  };

  const getMessageContent = () => {
    const { tipo, tarNome, } = notificacao;

    if (tipo === 'expirado') {
      return (
        <>
          <span className="font-semibold text-red-600">{tarNome} </span>
        </>
      );
    }

    if (tipo === 'comentario') {
      return (
        <>
          <span className="font-semibold text-gray-700">{tarNome}</span>.
        </>
      );
    }

    if (tipo === 'atribuido') {
      return (
        <>
          <span className="font-semibold text-blue-700">{tarNome}</span>
        </>
      );
    }

    else {
      return (
        <span className='font-semibold text-yellow-700'>{tarNome}</span>
      );
    };
  };

  return (
    <div className="flex justify-between items-start p-3 border-b border-gray-100 hover:bg-gray-50 cursor-pointer">
      <div className="flex items-start space-x-3 min-w-0">
        {getIcon()}
        <div className="px-4 text-sm flex-grow">
          <p className="text-gray-800 leading-snug break-words">
            {getMessageContent()}
          </p>
          <p className="text-xs text-gray-500 mt-1">
            {notificacao.tipo === 'expirado' ? 'Tarefa em atraso' : (notificacao.tipo === 'comentario' ? 'Novo comentário' : 'Nova tarefa atribuída')} • {notificacao.data}
          </p>
        </div>
      </div>
      <div className="ml-2 sm:ml-4 flex-shrink-0">
        <div
          className="text-gray-400 hover:text-gray-600 p-1 rounded-full transition-colors relative"
          onClick={(e) => {
            e.stopPropagation(); 
            setMenuAberto(!menuAberto);
          }}
        >
          <MoreVertical />
          {menuAberto && (
            <MenuNotificacao
              notificacao={notificacao}
              onClose={() => setMenuAberto(false)}
              onDelete={onDelete}
            />
          )}
        </div>
      </div>
    </div>
  );
};

export default CardNotificacao;