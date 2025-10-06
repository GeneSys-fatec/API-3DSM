export interface Tarefa {
  tar_id: string;
  tar_titulo: string;
  tar_status: string;
  usu_id: string;
  usu_nome: string;
  tar_prazo: string;
  tar_prioridade: "Alta" | "Média" | "Baixa";
  tar_descricao: string;
  tar_anexo?: File | null; 
}

export type NovaTarefa = Omit<Tarefa, "tar_id">;

export type Coluna = {
  id: string;
  titulo: string;
  ordem: number;
  corClasse: string;
  corFundo: string;
};

export type Usuario = {
    usu_id: string;
    usu_nome: string;
    usu_email?: string;
    usu_caminhoFoto?: string;
    usu_dataCriacao?: string;
    usu_dataAtualizacao?: string;
};

export type Projeto = {
  id: string;
  nome: string;
  descricao?: string;
  dataCriacao?: string;
};

export type Anexo = {
    arquivoNome: string;
    arquivoCaminho: string;
    arquivoTipo: string;
    arquivoTamanho?: number;
};