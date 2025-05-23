// src/pages/LivrosPage.jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function LivrosPage() {
    const navigate = useNavigate();
    const [livros, setLivros] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [mensagem, setMensagem] = useState(null);

    const fetchLivros = async () => {
        try {
            setLoading(true);
            setError(null);
            const response = await axios.get('http://localhost:8080/api/livros');
            setLivros(response.data);
        } catch (err) {
            console.error("Erro ao buscar livros:", err);
            if (err.response) {
                setError(`Erro do servidor: ${err.response.status} - ${err.response.data.message || 'Erro desconhecido'}`);
            } else if (err.request) {
                setError('Erro de rede: O back-end não está respondendo. Verifique se ele está rodando na porta 8080.');
            } else {
                setError(`Erro inesperado: ${err.message}`);
            }
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchLivros();
    }, []);

    const handleDeletarLivro = async (id) => {
        if (window.confirm(`Tem certeza que deseja deletar o livro com ID ${id}?`)) {
            try {
                await axios.delete(`http://localhost:8080/api/livros/${id}`);
                setMensagem('Livro deletado com sucesso!');
                fetchLivros();
            } catch (err) {
                console.error("Erro ao deletar livro:", err);
                setMensagem(`Erro ao deletar livro: ${err.response?.status} - ${err.response?.data?.message || 'Erro desconhecido'}`);
            }
        }
    };

    if (loading) {
        return <p>Carregando lista de livros...</p>;
    }

    if (error) {
        return <p style={{ color: 'red' }}>{error}</p>;
    }

    return (
       
        <div className="livros-header-actions"> 
            <h2>Livros Disponíveis</h2>

            {mensagem && (
                <p style={{ color: mensagem.includes('sucesso') ? 'green' : 'red' }}>
                    {mensagem}
                </p>
            )}

            <button onClick={() => navigate('/livros/novo')} style={{ marginBottom: '20px' }}>
                Adicionar Novo Livro
            </button>

            {livros.length === 0 ? (
                <p>Nenhum livro cadastrado no momento.</p>
            ) : (
                <ul>
                    {livros.map(livro => (
                        <li key={livro.id}>
                            <strong>Título:</strong> {livro.titulo || 'N/A'} <br />
                            <strong>Autor(es):</strong>{' '}
                            {livro.autores && livro.autores.length > 0
                                ? livro.autores.map(autor => autor.nome).join(', ')
                                : 'N/A'} <br />
                            <strong>ISBN:</strong> {livro.isbn || 'N/A'} <br />
                            <strong>Ano de Publicação:</strong> {livro.anoPublicacao || 'N/A'} <br />
                            <strong>Editora:</strong> {livro.editora || 'N/A'} <br />
                            <strong>Gênero:</strong> {livro.genero || 'N/A'} <br />
                            <strong>Cópias:</strong> {livro.numeroCopias || 'N/A'} <br />
                            <strong>Disponíveis:</strong> {livro.numeroCopiasDisponiveis || 'N/A'} <br />
                            <strong>Categoria:</strong> {livro.categoria ? livro.categoria.nome : 'N/A'} <br />
                            <button onClick={() => handleDeletarLivro(livro.id)}>Deletar</button>
                            {}
                            <hr />
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default LivrosPage;