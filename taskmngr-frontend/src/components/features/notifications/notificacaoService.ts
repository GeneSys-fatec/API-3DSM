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

  // mapeia os dados do backend pro formato que o front espera
  return data.map((backend: any) => ({
  id: backend._id,
  tipo:
    backend.notTipo === 'ATRIBUICAO'
      ? 'atribuido'
      : backend.notTipo === 'COMENTARIO'
      ? 'comentario'
      : 'expirado',
  tarNome: backend.notMensagem, // ou extrair só o título da tarefa da string
  data: new Date(backend.notDataCriacao).toLocaleString(),
  usuNome: backend.notUsuarioNome || '', // se não tiver, vai vazio
}));
};
