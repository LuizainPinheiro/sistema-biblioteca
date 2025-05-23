// src/pages/CategoriasPage.jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';

function CategoriasPage() {
    const [categorias, setCategorias] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [mensagem, setMensagem] = useState(null);
    const [novaCategoriaNome, setNovaCategoriaNome] = useState('');
    const [categoriaEditandoId, setCategoriaEditandoId] = useState(null); 
    const [categoriaEditandoNome, setCategoriaEditandoNome] = useState(''); 

    const fetchCategorias = async () => {
        try {
            setLoading(true);
            setError(null);
            const response = await axios.get('http://localhost:8080/api/categorias');
            setCategorias(response.data);
        } catch (err) {
            console.error("Erro ao buscar categorias:", err);
            setError(`Erro ao buscar categorias: ${err.response?.status} - ${err.response?.data?.message || 'Erro desconhecido'}`);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchCategorias();
    }, []);

    const handleAddCategoria = async (e) => {
        e.preventDefault();
        setMensagem(null);

        if (!novaCategoriaNome.trim()) {
            setMensagem('O nome da categoria é obrigatório.');
            return;
        }

        try {
            await axios.post('http://localhost:8080/api/categorias', { nome: novaCategoriaNome });
            setMensagem('Categoria adicionada com sucesso!');
            setNovaCategoriaNome('');
            fetchCategorias();
        } catch (err) {
            console.error('Erro ao adicionar categoria:', err);
            setMensagem(`Erro ao adicionar categoria: ${err.response?.status} - ${err.response?.data?.message || 'Erro desconhecido. Verifique se a categoria já existe.'}`);
        }
    };


    const handleEditClick = (categoria) => {
        setCategoriaEditandoId(categoria.id);
        setCategoriaEditandoNome(categoria.nome);
    };

    
    const handleCancelEdit = () => {
        setCategoriaEditandoId(null);
        setCategoriaEditandoNome('');
    };

    const handleSaveEdit = async (e) => {
        e.preventDefault();
        setMensagem(null);

        if (!categoriaEditandoNome.trim()) {
            setMensagem('O nome da categoria é obrigatório.');
            return;
        }

        try {
            await axios.put(`http://localhost:8080/api/categorias/${categoriaEditandoId}`, {
                id: categoriaEditandoId,
                nome: categoriaEditandoNome
            });
            setMensagem('Categoria atualizada com sucesso!');
            handleCancelEdit(); 
            fetchCategorias(); 
        } catch (err) {
            console.error('Erro ao atualizar categoria:', err);
            setMensagem(`Erro ao atualizar categoria: ${err.response?.status} - ${err.response?.data?.message || 'Erro desconhecido. Verifique se o nome já existe.'}`);
        }
    };

    const handleDeletarCategoria = async (id) => {
        if (window.confirm(`Tem certeza que deseja deletar a categoria com ID ${id}? Isso pode falhar se houver livros associados.`)) {
            setMensagem(null);
            try {
                await axios.delete(`http://localhost:8080/api/categorias/${id}`);
                setMensagem('Categoria deletada com sucesso!');
                fetchCategorias();
            } catch (err) {
                console.error("Erro ao deletar categoria:", err);
                setMensagem(`Erro ao deletar categoria: ${err.response?.status} - ${err.response?.data?.message || 'Erro desconhecido. Verifique se não há livros associados.'}`);
            }
        }
    };

    if (loading) {
        return <p>Carregando categorias...</p>;
    }

    if (error) {
        return <p style={{ color: 'red' }}>{error}</p>;
    }

    return (
        <main>
            <h2>Gerenciar Categorias</h2>

            {mensagem && (
                <p style={{ color: mensagem.includes('sucesso') ? 'green' : 'red' }}>
                    {mensagem}
                </p>
            )}

            <h3>Adicionar Nova Categoria</h3>
            <form onSubmit={handleAddCategoria}>
                <div>
                    <label htmlFor="novaCategoriaNome">Nome da Categoria:</label>
                    <input
                        type="text"
                        id="novaCategoriaNome"
                        value={novaCategoriaNome}
                        onChange={(e) => setNovaCategoriaNome(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Adicionar Categoria</button>
            </form>

            <h3>Lista de Categorias</h3>
            {categorias.length === 0 ? (
                <p>Nenhuma categoria cadastrada.</p>
            ) : (
                <ul>
                    {categorias.map(categoria => (
                        <li key={categoria.id}>
                            {categoriaEditandoId === categoria.id ? (
                             
                                <form onSubmit={handleSaveEdit}>
                                    <div>
                                        <label htmlFor={`editNome-${categoria.id}`}>Nome:</label>
                                        <input
                                            type="text"
                                            id={`editNome-${categoria.id}`}
                                            value={categoriaEditandoNome}
                                            onChange={(e) => setCategoriaEditandoNome(e.target.value)}
                                            required
                                        />
                                    </div>
                                    <button type="submit">Salvar</button>
                                    <button type="button" onClick={handleCancelEdit} style={{ marginLeft: '10px' }}>Cancelar</button>
                                </form>
                            ) : (
                            
                                <>
                                    <strong>ID:</strong> {categoria.id} <br />
                                    <strong>Nome:</strong> {categoria.nome} <br />
                                    <button onClick={() => handleEditClick(categoria)}>Editar</button>
                                    <button onClick={() => handleDeletarCategoria(categoria.id)} style={{ marginLeft: '10px' }}>Deletar</button>
                                </>
                            )}
                            <hr />
                        </li>
                    ))}
                </ul>
            )}
        </main>
    );
}

export default CategoriasPage;