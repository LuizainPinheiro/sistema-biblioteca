// src/pages/EmprestimosPage.jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';

function EmprestimosPage() {
    const [emprestimos, setEmprestimos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [mensagem, setMensagem] = useState(null);

    const [livroId, setLivroId] = useState('');
    const [usuarioId, setUsuarioId] = useState('');
    const [dataDevolucaoPrevista, setDataDevolucaoPrevista] = useState('');

    const [livrosDisponiveis, setLivrosDisponiveis] = useState([]);
    const [usuariosDisponiveis, setUsuariosDisponiveis] = useState([]);

    useEffect(() => {
        fetchEmprestimos();
        fetchLivrosAndUsuarios();
    }, []);

    const fetchEmprestimos = async () => {
        try {
            setLoading(true);
            setError(null);
            const response = await axios.get('http://localhost:8080/api/emprestimos');
            setEmprestimos(response.data);
        } catch (err) {
            console.error("Erro ao buscar empréstimos:", err);
            setError(`Erro ao buscar empréstimos: ${err.response?.status} - ${err.response?.data?.message || 'Erro desconhecido'}`);
        } finally {
            setLoading(false);
        }
    };

    const fetchLivrosAndUsuarios = async () => {
        try {
            const livrosResponse = await axios.get('http://localhost:8080/api/livros');
            setLivrosDisponiveis(livrosResponse.data);
            const usuariosResponse = await axios.get('http://localhost:8080/api/usuarios');
            setUsuariosDisponiveis(usuariosResponse.data);
        } catch (error) {
            console.error('Erro ao carregar livros ou usuários:', error);
            setMensagem('Erro ao carregar opções de livros/usuários.');
        }
    };

    const handleRealizarEmprestimo = async (e) => {
        e.preventDefault();
        setMensagem(null);
        try {
            const newEmprestimo = {
                livroId: parseInt(livroId),
                usuarioId: parseInt(usuarioId),
                dataDevolucaoPrevista: dataDevolucaoPrevista,
            };
            await axios.post('http://localhost:8080/api/emprestimos', newEmprestimo);
            setMensagem('Empréstimo realizado com sucesso!');
            setLivroId('');
            setUsuarioId('');
            setDataDevolucaoPrevista('');
            fetchEmprestimos(); 
            fetchLivrosAndUsuarios(); 
        } catch (err) {
            console.error('Erro ao realizar empréstimo:', err);
            setMensagem(`Erro ao realizar empréstimo: ${err.response?.status} - ${err.response?.data?.message || 'Erro desconhecido'}`);
        }
    };

    const handleRegistrarDevolucao = async (id) => {
        if (window.confirm(`Tem certeza que deseja registrar a devolução do empréstimo ${id}?`)) {
            try {
                await axios.put(`http://localhost:8080/api/emprestimos/${id}/devolver`);
                setMensagem('Devolução registrada com sucesso!');
                fetchEmprestimos(); 
                fetchLivrosAndUsuarios(); 
            } catch (err) {
                console.error("Erro ao registrar devolução:", err);
                setMensagem(`Erro ao registrar devolução: ${err.response?.status} - ${err.response?.data?.message || 'Erro desconhecido'}`);
            }
        }
    };

    const handleDeletarEmprestimo = async (id) => {
        if (window.confirm(`Tem certeza que deseja deletar o empréstimo ${id}? (Atenção: Apenas empréstimos devolvidos podem ser deletados)`)) {
            try {
                await axios.delete(`http://localhost:8080/api/emprestimos/${id}`);
                setMensagem('Empréstimo deletado com sucesso!');
                fetchEmprestimos();
            } catch (err) {
                console.error("Erro ao deletar empréstimo:", err);
                setMensagem(`Erro ao deletar empréstimo: ${err.response?.status} - ${err.response?.data?.message || 'Erro desconhecido'}`);
            }
        }
    };


    if (loading) {
        return <p>Carregando lista de empréstimos...</p>;
    }

    if (error) {
        return <p style={{ color: 'red' }}>{error}</p>;
    }

    return (
        <main>
            <h2>Gerenciar Empréstimos</h2>
            {mensagem && (
                <p style={{ color: mensagem.includes('sucesso') ? 'green' : 'red' }}>
                    {mensagem}
                </p>
            )}

            <h3>Realizar Novo Empréstimo</h3>
            <form onSubmit={handleRealizarEmprestimo}>
                <div>
                    <label htmlFor="livroId">Livro:</label>
                    <select
                        id="livroId"
                        value={livroId}
                        onChange={(e) => setLivroId(e.target.value)}
                        required
                    >
                        <option value="">Selecione um livro</option>
                        {livrosDisponiveis.map(livro => (
                            <option key={livro.id} value={livro.id}>
                                {livro.titulo} (Disponíveis: {livro.numeroCopiasDisponiveis})
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <label htmlFor="usuarioId">Usuário:</label>
                    <select
                        id="usuarioId"
                        value={usuarioId}
                        onChange={(e) => setUsuarioId(e.target.value)}
                        required
                    >
                        <option value="">Selecione um usuário</option>
                        {usuariosDisponiveis.map(usuario => (
                            <option key={usuario.id} value={usuario.id}>
                                {usuario.nome} ({usuario.email})
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <label htmlFor="dataDevolucaoPrevista">Data de Devolução Prevista:</label>
                    <input
                        type="date"
                        id="dataDevolucaoPrevista"
                        value={dataDevolucaoPrevista}
                        onChange={(e) => setDataDevolucaoPrevista(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Emprestar Livro</button>
            </form>

            <h3>Empréstimos Atuais</h3>
            {emprestimos.length === 0 ? (
                <p>Nenhum empréstimo cadastrado.</p>
            ) : (
                <ul>
                    {emprestimos.map(e => (
                        <li key={e.id}>
                            Livro: {e.livro?.titulo || 'N/A'} (ISBN: {e.livro?.isbn || 'N/A'}) <br />
                            Usuário: {e.usuario?.nome || 'N/A'} ({e.usuario?.email || 'N/A'}) <br />
                            Emprestado em: {new Date(e.dataEmprestimo).toLocaleDateString()} {new Date(e.dataEmprestimo).toLocaleTimeString()} <br />
                            Devolução Prevista: {new Date(e.dataDevolucaoPrevista).toLocaleDateString()} <br />
                            Devolução Real: {e.dataDevolucaoReal ? new Date(e.dataDevolucaoReal).toLocaleDateString() : 'Pendente'} <br />
                            {e.dataDevolucaoReal === null && ( 
                                <button onClick={() => handleRegistrarDevolucao(e.id)}>Registrar Devolução</button>
                            )}
                            <button onClick={() => handleDeletarEmprestimo(e.id)} style={{ marginLeft: '10px' }}>Deletar</button>
                            <hr />
                        </li>
                    ))}
                </ul>
            )}
        </main>
    );
}

export default EmprestimosPage;