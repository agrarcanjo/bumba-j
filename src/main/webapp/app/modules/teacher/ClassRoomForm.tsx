import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import { createClassRoom, updateClassRoom, getClassRoom, CreateClassRoomDTO } from 'app/services/classroom.service';
import './classroom-form.scss';

export const ClassRoomForm: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEditMode = !!id;

  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState<CreateClassRoomDTO>({
    name: '',
    language: 'ENGLISH',
    description: '',
  });

  useEffect(() => {
    if (isEditMode && id) {
      loadClassRoom(Number(id));
    }
  }, [id, isEditMode]);

  const loadClassRoom = async (classRoomId: number) => {
    try {
      setLoading(true);
      const data = await getClassRoom(classRoomId);
      setFormData({
        name: data.name,
        language: data.language,
        description: data.description || '',
      });
    } catch (error: any) {
      toast.error('Erro ao carregar turma');
      console.error('Error loading classroom:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!formData.name.trim()) {
      toast.error('Nome da turma é obrigatório');
      return;
    }

    try {
      setLoading(true);
      if (isEditMode && id) {
        await updateClassRoom(Number(id), formData);
        toast.success('Turma atualizada com sucesso!');
      } else {
        await createClassRoom(formData);
        toast.success('Turma criada com sucesso!');
      }
      navigate('/teacher/classes');
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Erro ao salvar turma';
      toast.error(errorMessage);
      console.error('Error saving classroom:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate('/teacher/classes');
  };

  if (loading && isEditMode) {
    return (
      <div className="classroom-form">
        <div className="loading">Carregando...</div>
      </div>
    );
  }

  return (
    <div className="classroom-form">
      <div className="form-container">
        <div className="form-header">
          <h1>{isEditMode ? 'Editar Turma' : 'Nova Turma'}</h1>
          <p className="form-subtitle">{isEditMode ? 'Atualize as informações da turma' : 'Preencha os dados para criar uma nova turma'}</p>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="name">
              Nome da Turma <span className="required">*</span>
            </label>
            <input
              type="text"
              id="name"
              value={formData.name}
              onChange={e => setFormData({ ...formData, name: e.target.value })}
              placeholder="Ex: Turma 5º Ano A"
              maxLength={200}
              required
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="language">
              Idioma <span className="required">*</span>
            </label>
            <select
              id="language"
              value={formData.language}
              onChange={e => setFormData({ ...formData, language: e.target.value })}
              required
              disabled={loading}
            >
              <option value="ENGLISH">Inglês</option>
              <option value="PORTUGUESE">Português</option>
              <option value="SPANISH">Espanhol</option>
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="description">Descrição</label>
            <textarea
              id="description"
              value={formData.description}
              onChange={e => setFormData({ ...formData, description: e.target.value })}
              placeholder="Descreva a turma (opcional)"
              maxLength={500}
              rows={4}
              disabled={loading}
            />
            <span className="char-count">{formData.description?.length || 0}/500</span>
          </div>

          <div className="form-actions">
            <button type="button" className="btn-secondary" onClick={handleCancel} disabled={loading}>
              Cancelar
            </button>
            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Salvando...' : isEditMode ? 'Atualizar' : 'Criar Turma'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ClassRoomForm;
