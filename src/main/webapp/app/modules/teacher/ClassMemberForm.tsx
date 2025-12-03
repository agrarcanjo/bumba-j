import React, { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import axios from 'axios';
import './class-member-form.scss';

interface User {
  id: number;
  login: string;
  firstName: string;
  lastName: string;
  email: string;
}

export const ClassMemberForm = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const classId = searchParams.get('classId');

  const [availableStudents, setAvailableStudents] = useState<User[]>([]);
  const [selectedStudents, setSelectedStudents] = useState<number[]>([]);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    if (classId) {
      loadAvailableStudents();
    }
  }, [classId]);

  const loadAvailableStudents = async () => {
    try {
      setLoading(true);
      const response = await axios.get<User[]>(`/api/teacher/classes/${classId}/available-students`);
      setAvailableStudents(response.data);
    } catch (error) {
      toast.error('Erro ao carregar alunos disponíveis');
      console.error('Error loading available students:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleToggleStudent = (studentId: number) => {
    setSelectedStudents(prev => {
      if (prev.includes(studentId)) {
        return prev.filter(id => id !== studentId);
      } else {
        return [...prev, studentId];
      }
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (selectedStudents.length === 0) {
      toast.warning('Selecione pelo menos um aluno');
      return;
    }

    try {
      setSubmitting(true);
      await axios.post(`/api/teacher/classes/${classId}/add-students`, {
        studentIds: selectedStudents,
      });
      toast.success(`${selectedStudents.length} aluno(s) adicionado(s) com sucesso!`);
      navigate(`/teacher/classes/${classId}`);
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Erro ao adicionar alunos';
      toast.error(errorMessage);
      console.error('Error adding students:', error);
    } finally {
      setSubmitting(false);
    }
  };

  const filteredStudents = availableStudents.filter(
    student =>
      student.login.toLowerCase().includes(searchTerm.toLowerCase()) ||
      student.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      student.lastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      student.email.toLowerCase().includes(searchTerm.toLowerCase()),
  );

  if (loading) {
    return (
      <div className="class-member-form">
        <div className="loading">Carregando alunos disponíveis...</div>
      </div>
    );
  }

  return (
    <div className="class-member-form">
      <div className="form-header">
        <h1>Adicionar Alunos à Turma</h1>
        <button onClick={() => navigate(`/teacher/classes/${classId}`)} className="btn-back">
          ← Voltar
        </button>
      </div>

      <form onSubmit={handleSubmit}>
        <div className="search-box">
          <input
            type="text"
            placeholder="Buscar alunos por nome, login ou email..."
            value={searchTerm}
            onChange={e => setSearchTerm(e.target.value)}
            className="search-input"
          />
        </div>

        <div className="students-selection">
          <div className="selection-header">
            <h3>Alunos Disponíveis ({filteredStudents.length})</h3>
            <span className="selected-count">{selectedStudents.length} selecionado(s)</span>
          </div>

          {filteredStudents.length === 0 ? (
            <div className="empty-state">
              <p>Nenhum aluno disponível para adicionar</p>
            </div>
          ) : (
            <div className="students-list">
              {filteredStudents.map(student => (
                <div
                  key={student.id}
                  className={`student-item ${selectedStudents.includes(student.id) ? 'selected' : ''}`}
                  onClick={() => handleToggleStudent(student.id)}
                >
                  <input
                    type="checkbox"
                    checked={selectedStudents.includes(student.id)}
                    onChange={() => handleToggleStudent(student.id)}
                    onClick={e => e.stopPropagation()}
                  />
                  <div className="student-info">
                    <div className="student-name">
                      {student.firstName} {student.lastName}
                    </div>
                    <div className="student-details">
                      <span className="student-login">{student.login}</span>
                      <span className="student-email">{student.email}</span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="form-actions">
          <button type="button" onClick={() => navigate(`/teacher/classes/${classId}`)} className="btn-cancel" disabled={submitting}>
            Cancelar
          </button>
          <button type="submit" className="btn-submit" disabled={submitting || selectedStudents.length === 0}>
            {submitting ? 'Adicionando...' : `Adicionar ${selectedStudents.length} Aluno(s)`}
          </button>
        </div>
      </form>
    </div>
  );
};

export default ClassMemberForm;
