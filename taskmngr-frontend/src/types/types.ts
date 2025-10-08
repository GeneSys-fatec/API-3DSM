export interface Tarefa {
  tarId: string;
  tarTitulo: string;
  tarStatus: string;
  usuId: string;
  usuNome: string;
  tarPrazo: string;
  tarPrioridade: "Alta" | "Média" | "Baixa";
  tarDescricao: string;
  tarAnexo?: File | null; 
}

export type NovaTarefa = Omit<Tarefa, "tarId">;

export type Coluna = {
  id: string;
  titulo: string;
  ordem: number;
  corClasse: string;
  corFundo: string;
};

export type Usuario = {
  usuId: string;
  usuNome: string;
  usuEmail?: string;
  usuCaminhoFoto?: string;
  usuDataCriacao?: string;
  usuDataAtualizacao?: string;
};

export type Projeto = {
  projId: string;
  projNome: string;
  projDescricao?: string;
  projDataCriacao?: string;
};

export type Anexo = {
    arquivoNome: string;
    arquivoCaminho: string;
    arquivoTipo: string;
    arquivoTamanho?: number;
};