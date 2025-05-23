// src/pages/AutoresPage.jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios'; 

function AutoresPage() {
    const [autores, setAutores] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [mensagem, setMensagem] = useState(null);
    const [novoAutorNome, setNovoAutorNome] = useState(''); 
    const [novoAutorBiografia, setNovoAutorBiografia] = useState(''); 
    const [autorEditandoId, setAutorEditandoId] = useState(null); 
    const [autorEditandoNome, setAutorEditandoNome] = useState(''); 
    const [autorEditandoBiografia, setAutorEditandoBiografia] = useState(''); 


   
    const fetchAutores = async () => {
        try {
            setLoading(true);
            setError(null); 
            const response = await axios.get('http://localhost:8080/api/autores');
            setAutores(response.data);
        } catch (err) {
            console.error("Erro ao buscar autores:", err);
            setError(`Erro ao buscar autores: ${err.response?.status} - ${err.response?.data?.message || 'Erro desconhecido'}`);
        } finally {
            setLoading(false);
        }
    };

   
    useEffect(() => {
        fetchAutores();
    }, []);

    const handleAddAutor = async (e) => {
        e.preventDefault(); 
        setMensagem(null); 

        if (!novoAutorNome.trim()) {
            setMensagem('O nome do autor é obrigatório.');
            return;
        }

        try {
            
            await axios.post('http://localhost:8080/api/autores', {
                nome: novoAutorNome,
                biografia: novoAutorBiografia 
            });
            setMensagem('Autor adicionado com sucesso!');
            setNovoAutorNome(''); 
            setNovoAutorBiografia(''); 
            fetchAutores(); 
        } catch (err) {
            console.error('Erro ao adicionar autor:', err);
            setMensagem(`Erro ao adicionar autor: ${err.response?.status} - ${err.response?.data?.message || 'Erro desconhecido. Verifique se o autor já existe.'}`);
        }
    };

    
    const handleEditClick = (autor) => {
        setAutorEditandoId(autor.id);
        setAutorEditandoNome(autor.nome);
        setAutorEditandoBiografia(autor.biografia || ''); 
    };


    const handleCancelEdit = () => {
        setAutorEditandoId(null);
        setAutorEditandoNome('');
        setAutorEditandoBiografia('');
    };

   
    const handleSaveEdit = async (e) => {
        e.preventDefault();
        setMensagem(null);

        if (!autorEditandoNome.trim()) {
            setMensagem('O nome do autor é obrigatório.');
            return;
        }

        try {
            await axios.put(`http://localhost:8080/api/autores/${autorEditandoId}`, {
                id: autorEditandoId,
                nome: autorEditandoNome,
                biografia: autorEditandoBiografia
            });
            setMensagem('Autor atualizado com sucesso!');
            handleCancelEdit(); 
            fetchAutores(); 
        } catch (err) {
            console.error('Erro ao atualizar autor:', err);
            setMensagem(`Erro ao atualizar autor: ${err.response?.status} - ${err.response?.data?.message || 'Erro desconhecido. Verifique se o nome já existe.'}`);
        }
    };

   
    const handleDeletarAutor = async (id) => {
        if (window.confirm(`Tem certeza que deseja deletar o autor com ID ${id}? Isso pode falhar se houver livros associados.`)) {
            setMensagem(null);
            try {
                await axios.delete(`http://localhost:8080/api/autores/${id}`);
                setMensagem('Autor deletado com sucesso!');
                fetchAutores(); 
            } catch (err) {
                console.error("Erro ao deletar autor:", err);
                setMensagem(`Erro ao deletar autor: ${err.response?.status} - ${err.response?.data?.message || 'Erro desconhecido. Verifique se não há livros associados.'}`);
            }
        }
    };

    if (loading) {
        return <p>Carregando autores...</p>;
    }

    if (error) {
        return <p style={{ color: 'red' }}>{error}</p>;
    }

    return (
        <main>
            <h2>Gerenciar Autores</h2>

            {mensagem && (
                <p style={{ color: mensagem.includes('sucesso') ? 'green' : 'red' }}>
                    {mensagem}
                </p>
            )}

            <h3>Adicionar Novo Autor</h3>
            <form onSubmit={handleAddAutor}>
                <div>
                    <label htmlFor="novoAutorNome">Nome do Autor:</label>
                    <input
                        type="text"
                        id="novoAutorNome"
                        value={novoAutorNome}
                        onChange={(e) => setNovoAutorNome(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="novoAutorBiografia">Biografia:</label>
                    <textarea
                        id="novoAutorBiografia"
                        value={novoAutorBiografia}
                        onChange={(e) => setNovoAutorBiografia(e.target.value)}
                        rows="4" 
                    ></textarea>
                </div>
                <button type="submit">Adicionar Autor</button>
            </form>

            <h3>Lista de Autores</h3>
            {autores.length === 0 ? (
                <p>Nenhum autor cadastrado.</p>
            ) : (
                <ul>
                    {autores.map(autor => (
                        <li key={autor.id}>
                            {autorEditandoId === autor.id ? (
                                <form onSubmit={handleSaveEdit}>
                                    <div>
                                        <label htmlFor={`editNome-${autor.id}`}>Nome:</label>
                                        <input
                                            type="text"
                                            id={`editNome-${autor.id}`}
                                            value={autorEditandoNome}
                                            onChange={(e) => setAutorEditandoNome(e.target.value)}
                                            required
                                        />
                                    </div>
                                    <div>
                                        <label htmlFor={`editBiografia-${autor.id}`}>Biografia:</label>
                                        <textarea
                                            id={`editBiografia-${autor.id}`}
                                            value={autorEditandoBiografia}
                                            onChange={(e) => setAutorEditandoBiografia(e.target.value)}
                                            rows="4"
                                        ></textarea>
                                    </div>
                                    <button type="submit">Salvar</button>
                                    <button type="button" onClick={handleCancelEdit} style={{ marginLeft: '10px' }}>Cancelar</button>
                                </form>
                            ) : (
                               
                                <>
                                    <strong>ID:</strong> {autor.id} <br />
                                    <strong>Nome:</strong> {autor.nome} <br />
                                    {autor.biografia && (
                                        <><strong>Biografia:</strong> {autor.biografia} <br /></>
                                    )}
                                    <button onClick={() => handleEditClick(autor)}>Editar</button>
                                    <button onClick={() => handleDeletarAutor(autor.id)} style={{ marginLeft: '10px' }}>Deletar</button>
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

export default AutoresPage;