import React, { useState, useRef, useEffect } from 'react';
import { FixedSizeList as List } from 'react-window';
import AutoSizer from 'react-virtualized-auto-sizer';
import * as S from './styles';
import {
  useListarFiliaisQuery,
  useGetProdutosQuery,
  useGetProdutosByNameQuery,
  useUpdateProdutoMutation,
  useDeleteProdutoMutation
} from '../../services/api';
import { BrowserMultiFormatReader } from '@zxing/browser';
import { Input } from './styles';
import { ProdutoProps } from '../../services/api';
import Loader from '../Loader';

const Stock = () => {
  const [activeTab, setActiveTab] = useState<'manual' | 'codigo'>('manual');
  const [codigoBarras, setCodigoBarras] = useState('');
  const [produtoNome, setProdutoNome] = useState('');
  const [produtoPreco, setProdutoPreco] = useState('');
  const [mensagem, setMensagem] = useState('');
  const [editingProduto, setEditingProduto] = useState<number | null>(null);
  const [editNome, setEditNome] = useState('');
  const [editCodigoBarras, setEditCodigoBarras] = useState('');
  const [editNcm, setEditNcm] = useState('');
  const [editDescricao, setEditDescricao] = useState('');
  const [editPrecoUnitario, setEditPrecoUnitario] = useState('');
  const [editEan, setEditEan] = useState<string>('');
  const [editAtivo, setEditAtivo] = useState(true);
  const [editDataVencimento, setEditDataVencimento] = useState('');
  const [editQuantidade, setEditQuantidade] = useState(0);
  const [editObservacao, setEditObservacao] = useState<string | null>(null);
  const [editPrecoCusto, setEditPrecoCusto] = useState(0);
  const [editCategoriaId, setEditCategoriaId] = useState(0);
  const [editCfop, setEditCfop] = useState('');
  const [editUnidade, setEditUnidade] = useState('');
  const [editValorUnitarioComercial, setEditValorUnitarioComercial] = useState(0);
  const [editValorUnitarioDesconto, setEditValorUnitarioDesconto] = useState(0);
  const [editValorUnitarioTotal, setEditValorUnitarioTotal] = useState(0);
  const [editValorUnitarioTributavel, setEditValorUnitarioTributavel] = useState(0);
  const [editImagem, setEditImagem] = useState<string | null>(null);
  const [imagemExistente, setImagemExistente] = useState<string | null>(null);
  const [novaImagem, setNovaImagem] = useState<string | null>(null);

  const [editImpostos, setEditImpostos] = useState<Array<{
    tipoImposto: string;
    aliquota: number;
    cst: string;
    origem: string;
    csosn: string;
  }>>([]);
  const [searchTerm, setSearchTerm] = useState('');

  const { data: filiais, isLoading: loadingFiliais, error: errorFiliais } = useListarFiliaisQuery();
  const { data: produtos, isLoading: loadingProdutos, error: errorProdutos, refetch } = useGetProdutosQuery();
  const { data: searchedProdutos, isLoading: loadingSearch, refetch: refetchSearch } = useGetProdutosByNameQuery(searchTerm, {
    skip: searchTerm === ''
  });
  const [updateProduto] = useUpdateProdutoMutation();
  const [deleteProduto] = useDeleteProdutoMutation();

  const videoRef = useRef<HTMLVideoElement | null>(null);
  const [scanning, setScanning] = useState(false);
  const codeReader = useRef<BrowserMultiFormatReader | null>(null);

  useEffect(() => {
    codeReader.current = new BrowserMultiFormatReader();
    return () => {
      if (videoRef.current && videoRef.current.srcObject) {
        const stream = videoRef.current.srcObject as MediaStream;
        stream.getTracks().forEach((track) => track.stop());
      }
    };
  }, []);

  const imagemFinal: string | null =
    (novaImagem?.trim() ?? '') !== '' ? novaImagem : (imagemExistente ?? null);

  const iniciarLeituraCodigo = async () => {
    if (!videoRef.current || !codeReader.current) return;
    setScanning(true);

    try {
      const result = await codeReader.current.decodeOnceFromVideoDevice(undefined, videoRef.current);
      const codigo = result.getText();
      setCodigoBarras(codigo);

      const produtoEncontrado = produtosParaExibir.find(p => p.ean === codigo);

      if (!produtoEncontrado) {
        setProdutoNome('');
        setProdutoPreco('');
        setMensagem('❌ Produto não encontrado.');
        return;
      }

      setProdutoNome(produtoEncontrado.nome || '');
      setProdutoPreco(produtoEncontrado.precoUnitario?.toString() || '');
      setMensagem('✅ Produto carregado com sucesso!');
    } catch (error) {
      console.error('Erro ao ler código:', error);
      setMensagem('❌ Erro ao ler o código ou buscar produto.');
    } finally {
      setScanning(false);

      if (videoRef.current?.srcObject) {
        const stream = videoRef.current.srcObject as MediaStream;
        stream.getTracks().forEach((track) => track.stop());
      }
    }
  };

  const pararLeituraCodigo = () => {
    if (videoRef.current && videoRef.current.srcObject) {
      const stream = videoRef.current.srcObject as MediaStream;
      stream.getTracks().forEach((track) => track.stop());
    }
    setScanning(false);
  };

  const handleBuscarProdutos = async () => {
    try {
      await refetch();
      setMensagem('✅ Produtos atualizados com sucesso!');
    } catch (error) {
      console.error('Erro ao buscar produtos:', error);
      setMensagem('❌ Erro ao buscar produtos.');
    }
  };

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    refetchSearch();
  };

  const handleEditarProduto = (produto: ProdutoProps) => {
    setEditingProduto(produto.id || null);
    setEditNome(produto.nome);
    setEditDescricao(produto.descricao);
    setEditPrecoUnitario(produto.precoUnitario.toString());
    setEditPrecoCusto(produto.precoCusto);
    setEditEan(produto.ean);
    setEditNcm(produto.ncm);
    setEditDataVencimento(produto.dataVencimento);
    setEditAtivo(produto.ativo);
    setEditQuantidade(Number(produto.quantidade));
    setEditObservacao(produto.observacao || '');
    setEditCategoriaId(produto.categoriaId);
    setEditCfop(produto.cfop);
    setEditUnidade(produto.unidade);
    setEditValorUnitarioComercial(produto.valorUnitarioComercial);
    setEditValorUnitarioDesconto(produto.valorUnitarioDesconto);
    setEditValorUnitarioTotal(produto.valorUnitarioTotal);
    setEditValorUnitarioTributavel(produto.valorUnitarioTributavel);
    setEditImpostos(produto.impostos || []);
    setEditImagem(produto.imagem || null);
    setImagemExistente(produto.imagem || null);
  };

  const handleCancelarEdicao = () => {
    setEditingProduto(null);
    setEditNome('');
    setEditDescricao('');
    setEditPrecoUnitario('');
    setEditPrecoCusto(0);
    setNovaImagem('');
    setEditEan('');
    setEditNcm('');
    setEditAtivo(true);
    setEditDataVencimento('');
    setEditQuantidade(0);
    setEditObservacao(null);
    setEditCategoriaId(0);
    setEditCfop('');
    setEditUnidade('');
    setEditValorUnitarioComercial(0);
    setEditValorUnitarioDesconto(0);
    setEditValorUnitarioTotal(0);
    setEditValorUnitarioTributavel(0);
    setEditImpostos([]);
  };

  const handleSalvarEdicao = async (id: number) => {
    try {
      const imagemFinal = (novaImagem?.trim() ?? '') !== ''
        ? novaImagem
        : imagemExistente ?? undefined;

      const produtoAtualizado: ProdutoProps = {
        id,
        nome: editNome,
        descricao: editDescricao,
        precoUnitario: editPrecoUnitario,
        precoCusto: editPrecoCusto,
        ean: editEan,
        categoriaId: editCategoriaId,
        ncm: editNcm,
        dataVencimento: editDataVencimento,
        ativo: editAtivo,
        quantidade: editQuantidade.toString(),
        observacao: editObservacao || '',
        cfop: editCfop,
        unidade: editUnidade,
        valorUnitarioComercial: editValorUnitarioComercial,
        valorUnitarioDesconto: editValorUnitarioDesconto,
        valorUnitarioTotal: editValorUnitarioTotal,
        valorUnitarioTributavel: editValorUnitarioTributavel,
        impostos: editImpostos,
        produtoId: undefined,
        imagem: imagemFinal ?? null
      };

      await updateProduto({
        id,
        data: produtoAtualizado
      }).unwrap();

      setMensagem('✅ Produto atualizado com sucesso!');
      setEditingProduto(null);
      refetch();
      if (searchTerm) refetchSearch();
    } catch (error) {
      console.error('Erro ao atualizar produto:', error);
      setMensagem('❌ Erro ao atualizar produto');
    }
  };

  const handleExcluirProduto = async (id: number) => {
    if (window.confirm('Tem certeza que deseja excluir este produto?')) {
      try {
        await deleteProduto(id).unwrap();
        setMensagem('✅ Produto excluído com sucesso!');
        refetch();
        if (searchTerm) refetchSearch();
      } catch (error) {
        console.error('Erro ao excluir produto:', error);
        setMensagem('❌ Erro ao excluir produto');
      }
    }
  };

  const produtosParaExibir = searchTerm ? searchedProdutos || [] : produtos || [];

  return (
    <S.Container>
      <div>
        <S.Title>Consulta de Produtos</S.Title>

        <S.Form onSubmit={handleSearch} style={{ marginBottom: '20px' }}>
          <S.ContainerSerch style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '10px' }}>
            <S.Input
              type="text"
              placeholder="Buscar por nome..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <S.Button
              type="button"
              onClick={() => {
                setSearchTerm('');
                handleBuscarProdutos();
              }}
            >
              Limpar Busca
            </S.Button>
          </S.ContainerSerch>
        </S.Form>

        <S.TabContainer>
          <S.TabButton active={activeTab === 'manual'} onClick={() => setActiveTab('manual')}>Manual</S.TabButton>
          <S.TabButton active={activeTab === 'codigo'} onClick={() => setActiveTab('codigo')}>Código de Barras</S.TabButton>
        </S.TabContainer>

        {activeTab === 'manual' && (
          <div>
            {loadingProdutos || (searchTerm && loadingSearch) ? (
              <span><Loader /></span>
            ) : errorProdutos ? (
              <p>Erro ao carregar produtos</p>
            ) : (
              <>
                <h3 style={{ textAlign: 'center' }}>
                  {searchTerm ? `Resultados para "${searchTerm}"` : 'Lista de Produtos'}
                </h3>

                <S.ProdutosList>
                  {produtosParaExibir.map((produto) => (
                    <S.ProdutoItem key={produto.id}>
                      {editingProduto === produto.id ? (
                        <S.Form>
                          <S.Label>Nome:</S.Label>
                          <Input
                            type="text"
                            value={editNome}
                            onChange={(e) => setEditNome(e.target.value)}
                          />

                          <S.Label>Descrição:</S.Label>
                          <Input
                            type="text"
                            value={editDescricao}
                            onChange={(e) => setEditDescricao(e.target.value)}
                          />

                          <S.Label>Preço Unitário:</S.Label>
                          <Input
                            type="text"
                            value={editPrecoUnitario}
                            onChange={(e) => {
                              const valorDigitado = e.target.value;

                              // Substitui vírgula por ponto
                              const valorTratado = valorDigitado.replace(',', '.');

                              // Aceita apenas números e um único ponto decimal
                              const regex = /^\d*\.?\d*$/;

                              if (valorTratado === '' || regex.test(valorTratado)) {
                                setEditPrecoUnitario(valorTratado);
                              }
                            }}
                          />
                          <S.Label>Preço de Custo:</S.Label>
                          <Input
                            type="text"
                            value={editPrecoCusto}
                            onChange={(e) => {
                              const valorDigitado = e.target.value.replace(',', '.');
                              const numero = parseFloat(valorDigitado);

                              // Só atualiza se for número ou vazio
                              if (!isNaN(numero) || valorDigitado === '') {
                                setEditPrecoCusto(editPrecoCusto);
                              }
                            }}
                          />
                          <S.Label>EAN:</S.Label>
                          <Input
                            type="text"
                            value={editEan.toString()}
                            onChange={(e) => {
                              const value = e.target.value;
                              const numericValue = value.replace(/\D/g, '');
                              setEditEan(numericValue);
                            }}
                            pattern="\d*" // Sugere entrada numérica em dispositivos móveis
                          />
                          <S.Label>NCM:</S.Label>
                          <Input
                            type="text"
                            value={editNcm}
                            onChange={(e) => setEditNcm(e.target.value)}
                          />

                          <S.Label>Ativo:</S.Label>
                          <Input
                            type="checkbox"
                            checked={editAtivo}
                            onChange={(e) => setEditAtivo(e.target.checked)}
                          />

                          <S.Label>Data de Vencimento:</S.Label>
                          <Input
                            type="date"
                            value={editDataVencimento}
                            onChange={(e) => setEditDataVencimento(e.target.value)}
                          />

                          <S.Label>Quantidade:</S.Label>
                          <Input
                            type="number"
                            value={editQuantidade}
                            onChange={(e) => setEditQuantidade(Number(e.target.value))}
                          />

                          <S.Label>Observação:</S.Label>
                          <Input
                            type="text"
                            value={editObservacao || ''}
                            onChange={(e) => setEditObservacao(e.target.value)}
                          />

                          <S.Label>CFOP:</S.Label>
                          <Input
                            type="text"
                            value={editCfop}
                            onChange={(e) => setEditCfop(e.target.value)}
                          />
                          <S.Label>Categoria:</S.Label>
                          <Input
                            type="text"
                            value={editCategoriaId}
                            onChange={(e) => setEditCfop(e.target.value)}
                          />
                          <S.Label>Valor unitário comercial:</S.Label>
                          <Input
                            type="text"
                            value={editValorUnitarioComercial}
                            onChange={(e) => {
                              const valor = e.target.value.replace(',', '.');
                              if (/^\d*\.?\d*$/.test(valor) || valor === '') {
                                setEditValorUnitarioComercial(valor === '' ? 0 : parseFloat(valor));
                              }
                            }}
                          />

                          <S.Label>Valor unitário desconto:</S.Label>
                          <Input
                            type="text"
                            value={editValorUnitarioDesconto}
                            onChange={(e) => {
                              const valor = e.target.value.replace(',', '.');
                              if (/^\d*\.?\d*$/.test(valor) || valor === '') {
                                setEditValorUnitarioDesconto(valor === '' ? 0 : parseFloat(valor));
                              }
                            }}
                          />

                          <S.Label>Valor unitário total:</S.Label>
                          <Input
                            type="text"
                            value={editValorUnitarioTotal}
                            onChange={(e) => {
                              const valor = e.target.value.replace(',', '.');
                              if (/^\d*\.?\d*$/.test(valor) || valor === '') {
                                setEditValorUnitarioTotal(valor === '' ? 0 : parseFloat(valor));
                              }
                            }}
                          />

                          <S.Label>Valor unitário tributável:</S.Label>
                          <Input
                            type="text"
                            value={editValorUnitarioTributavel}
                            onChange={(e) => {
                              const valor = e.target.value.replace(',', '.');
                              if (/^\d*\.?\d*$/.test(valor) || valor === '') {
                                setEditValorUnitarioTributavel(valor === '' ? 0 : parseFloat(valor));
                              }
                            }}
                          />
                          <S.Label>Unidade:</S.Label>
                          <Input
                            type="text"
                            value={editUnidade}
                            onChange={(e) => setEditUnidade(e.target.value)}
                          />
                          <S.Label>Nova Imagem (URL ou base64):</S.Label>
                          <Input
                            type="text"
                            value={novaImagem ?? ''}
                            onChange={(e) => setNovaImagem(e.target.value)}
                            placeholder="Deixe em branco para manter a imagem atual"
                          />
                          <S.ButtonGroup>
                            <S.ActionButton
                              type="button"
                              onClick={() => handleSalvarEdicao(produto.id!)}
                              color="#3b82f6"
                            >
                              Salvar
                            </S.ActionButton>
                            <S.ActionButton
                              type="button"
                              onClick={handleCancelarEdicao}
                              color="#ef4444"
                            >
                              Cancelar
                            </S.ActionButton>
                          </S.ButtonGroup>
                        </S.Form>
                      ) : (
                        <>
                          {produto.imagem && (
                            <img
                              src={produto.imagem}
                              alt="Produto"
                              loading="lazy"
                              style={{ width: "100%", height: "120px", objectFit: "contain", borderRadius: '0.5rem' }}
                            />
                          )}
                          <div><strong>NCM:</strong> {produto.ncm}</div>
                          <div><strong>Nome:</strong> {produto.nome}</div>
                          <div><strong>Descrição:</strong> {produto.descricao}</div>
                          <div><strong>Preço:</strong> R$ {produto.precoUnitario}</div>
                          <div><strong>EAN:</strong> {produto.ean}</div>
                          <div><strong>Status:</strong> {produto.ativo ? 'Ativo' : 'Inativo'}</div>
                          <div><strong>Vencimento:</strong> {produto.dataVencimento}</div>
                          <div><strong>Quantidade:</strong> {produto.quantidade}</div>
                          <div><strong>Observação:</strong> {produto.observacao || 'Nenhuma'}</div>
                          <S.ButtonGroup>
                            <S.ActionButton
                              type="button"
                              onClick={() => handleEditarProduto(produto)}
                              color="#3b82f6"
                            >
                              Editar
                            </S.ActionButton>
                            <S.ActionButton
                              type="button"
                              onClick={() => handleExcluirProduto(produto.id!)}
                              color="#ef4444"
                            >
                              Excluir
                            </S.ActionButton>
                          </S.ButtonGroup>
                        </>
                      )}
                    </S.ProdutoItem>
                  ))}
                </S.ProdutosList>
              </>
            )}
          </div>
        )}

        {activeTab === 'codigo' && (
          <S.Form onSubmit={(e) => e.preventDefault()}>
            <S.Input
              type="text"
              placeholder="Digite o código de barras"
              value={codigoBarras}
              onChange={(e) => setCodigoBarras(e.target.value)}
              required
            />

            {!scanning ? (
              <S.Button type="button" onClick={iniciarLeituraCodigo}>
                📷 Ler código pela câmera
              </S.Button>
            ) : (
              <S.Button type="button" onClick={pararLeituraCodigo}>
                ✋ Parar leitura
              </S.Button>
            )}

            <video
              ref={videoRef}
              style={{ width: '100%', maxHeight: '300px', margin: '10px 0', display: scanning ? 'block' : 'none' }}
            />

            {codigoBarras && (
              <div>
                <h4>Informações do Produto</h4>
                {produtoNome ? (
                  <>
                    <p>Nome: {produtoNome}</p>
                    <p>Preço: R$ {produtoPreco}</p>
                  </>
                ) : (
                  <p>Nenhum produto encontrado com este código</p>
                )}
              </div>
            )}

            {mensagem && <S.Mensagem tipo="erro">{mensagem}</S.Mensagem>}
          </S.Form>
        )}
      </div>
    </S.Container>
  );
};

export default Stock;