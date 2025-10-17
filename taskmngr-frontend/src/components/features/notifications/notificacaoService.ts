import { Notificacao } from '@/types/types';
import { authFetch } from '@/utils/api';

export const buscarNotificacoes = async (): Promise<Notificacao[]> => {
  const resposta = await authFetch('http://localhost:8080/notificacao/listar', {
    method: 'GET',
  });

  if (!resposta.ok) {
    throw new Error('Erro ao buscar notificações');
  }

  const data = await resposta.json();

  return data.map((backend: any) => ({
  id: backend.notId, 
  tipo:
    backend.notTipo === 'ATRIBUICAO'
      ? 'atribuido'
      : backend.notTipo === 'COMENTARIO'
      ? 'comentario'
      : 'expirado',
  tarNome: backend.notMensagem, 
  data: new Date(backend.notDataCriacao).toLocaleString(),
  usuNome: backend.notUsuarioNome || '', 
}));
};

export const buscarNotificacoesNaoLidas = async (): Promise<boolean> => {
  const resposta = await authFetch('http://localhost:8080/notificacao/listar', { method: 'GET' });

  if (!resposta.ok) {
    console.error('Erro ao buscar notificações');
    return false;
  }

  const notificacoes: Notificacao[] = await resposta.json();
  return notificacoes.some((n) => !n.notLida);
};

export const marcarTodasComoLidas = async (): Promise<void> => {
  const resposta = await authFetch('http://localhost:8080/notificacao/marcar-todas', {
    method: 'PUT',
  });

  if (!resposta.ok) {
    console.error('Erro ao marcar notificações como lidas');
  }
};

export const deletarNotificacao = async (id: string): Promise<void> => {
  const resposta = await authFetch(`http://localhost:8080/notificacao/${id}`, {
    method: 'DELETE',
  });

  if (!resposta.ok) {
    console.error('Erro ao deletar notificação');
  }
};
