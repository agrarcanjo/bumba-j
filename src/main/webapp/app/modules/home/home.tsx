import './home.scss';

import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faCheck,
  faUsers,
  faChartLine,
  faGraduationCap,
  faSchool,
  faUserTie,
  faChalkboardTeacher,
  faPlus,
  faMinus,
} from '@fortawesome/free-solid-svg-icons';

import { useAppSelector } from 'app/config/store';
import { AUTHORITIES } from 'app/config/constants';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const navigate = useNavigate();
  const [openFaq, setOpenFaq] = useState<number | null>(null);

  useEffect(() => {
    if (isAuthenticated && account?.authorities?.includes(AUTHORITIES.ROLE_STUDENT)) {
      navigate('/student/dashboard');
    }
  }, [isAuthenticated, account, navigate]);

  const toggleFaq = (index: number) => {
    setOpenFaq(openFaq === index ? null : index);
  };

  const handleContactSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // TODO: Implement contact form submission
    alert('Formulário enviado! Entraremos em contato em breve.');
  };

  if (isAuthenticated && account?.authorities?.includes(AUTHORITIES.ROLE_STUDENT)) {
    return null;
  }

  return (
    <div className="landing-page">
      {/* Hero Section */}
      <section className="hero-section">
        <div className="hero-content">
          <div className="hero-text">
            <p className="hero-tagline">
              <Translate contentKey="home.hero.tagline">Educação Bilíngue ao Alcance de Todos</Translate>
            </p>
            <h1>
              <Translate contentKey="home.hero.title">Inglês de qualidade para toda a sua rede pública</Translate>
            </h1>
            <p className="hero-subtitle">
              <Translate contentKey="home.hero.subtitle">
                Uma plataforma gamificada inspirada no Bumba meu Boi, que engaja estudantes, apoia professores e oferece dados em tempo real
                para redes municipais e estaduais.
              </Translate>
            </p>
            <div className="hero-ctas">
              <Link to="/contact" className="btn-primary">
                <Translate contentKey="home.hero.cta.primary">Quero conhecer o Bumba Learning</Translate>
              </Link>
              <a href="#" className="btn-secondary">
                <Translate contentKey="home.hero.cta.secondary">Baixar apresentação para secretarias</Translate>
              </a>
            </div>
            <p className="trust-badge">
              <Translate contentKey="home.hero.trust">Feito no Brasil, pensado para escolas públicas do Nordeste</Translate>
            </p>
          </div>
          <div className="hero-visual">
            <div>
              <div className="bumba-mascot"></div>
              <div className="mockup-screens">
                <div className="screen-mockup">
                  <h4>Dashboard do Aluno</h4>
                  <div className="xp-bar"></div>
                  <p>XP: 1,250 | Nível: Intermediate</p>
                </div>
                <div className="screen-mockup">
                  <h4>Ranking Municipal</h4>
                  <p>🏆 1º Lugar - Escola Santos Dumont</p>
                  <p>🥈 2º Lugar - Escola José de Alencar</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Reality Section */}
      <section className="section reality-section">
        <div className="section-container">
          <div className="section-header">
            <h2>
              <Translate contentKey="home.reality.title">Pensado para a realidade das escolas públicas do Nordeste</Translate>
            </h2>
            <p>
              <Translate contentKey="home.reality.subtitle">Uma solução que funciona na infraestrutura que você já tem</Translate>
            </p>
          </div>
          <div className="benefits-grid">
            <div className="benefit-item">
              <div className="benefit-icon">
                <FontAwesomeIcon icon={faCheck} />
              </div>
              <p className="benefit-text">
                <Translate contentKey="home.reality.benefits.infrastructure">
                  Funciona bem na infraestrutura típica de escolas públicas
                </Translate>
              </p>
            </div>
            <div className="benefit-item">
              <div className="benefit-icon">
                <FontAwesomeIcon icon={faCheck} />
              </div>
              <p className="benefit-text">
                <Translate contentKey="home.reality.benefits.pace">Alunos aprendem no próprio ritmo, com lições curtas</Translate>
              </p>
            </div>
            <div className="benefit-item">
              <div className="benefit-icon">
                <FontAwesomeIcon icon={faCheck} />
              </div>
              <p className="benefit-text">
                <Translate contentKey="home.reality.benefits.teachers">Professores ganham dados e tempo para focar na mediação</Translate>
              </p>
            </div>
            <div className="benefit-item">
              <div className="benefit-icon">
                <FontAwesomeIcon icon={faCheck} />
              </div>
              <p className="benefit-text">
                <Translate contentKey="home.reality.benefits.access">
                  Acesso em computadores simples, laboratórios de informática e tablets
                </Translate>
              </p>
            </div>
            <div className="benefit-item">
              <div className="benefit-icon">
                <FontAwesomeIcon icon={faCheck} />
              </div>
              <p className="benefit-text">
                <Translate contentKey="home.reality.benefits.internet">Aulas leves que funcionam com internet limitada</Translate>
              </p>
            </div>
            <div className="benefit-item">
              <div className="benefit-icon">
                <FontAwesomeIcon icon={faCheck} />
              </div>
              <p className="benefit-text">
                <Translate contentKey="home.reality.benefits.content">Conteúdos gamificados para a faixa etária dos estudantes</Translate>
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* How It Works Section */}
      <section className="section">
        <div className="section-container">
          <div className="section-header">
            <h2>
              <Translate contentKey="home.howItWorks.title">Como o Bumba Learning funciona na prática</Translate>
            </h2>
            <p>
              <Translate contentKey="home.howItWorks.subtitle">Implementação simples em 4 passos</Translate>
            </p>
          </div>
          <div className="steps-timeline">
            <div className="step-item">
              <div className="step-number">1</div>
              <h3>
                <Translate contentKey="home.howItWorks.steps.step1.title">Secretaria escolhe turmas</Translate>
              </h3>
              <p>
                <Translate contentKey="home.howItWorks.steps.step1.description">
                  Secretaria/escola escolhe turmas e anos para participar do programa
                </Translate>
              </p>
            </div>
            <div className="step-item">
              <div className="step-number">2</div>
              <h3>
                <Translate contentKey="home.howItWorks.steps.step2.title">Formação dos professores</Translate>
              </h3>
              <p>
                <Translate contentKey="home.howItWorks.steps.step2.description">
                  Professores recebem formação rápida e acesso ao painel de gestão
                </Translate>
              </p>
            </div>
            <div className="step-item">
              <div className="step-number">3</div>
              <h3>
                <Translate contentKey="home.howItWorks.steps.step3.title">Alunos começam a usar</Translate>
              </h3>
              <p>
                <Translate contentKey="home.howItWorks.steps.step3.description">
                  Alunos começam a usar o app em laboratório ou sala multimídia
                </Translate>
              </p>
            </div>
            <div className="step-item">
              <div className="step-number">4</div>
              <h3>
                <Translate contentKey="home.howItWorks.steps.step4.title">Acompanhamento de resultados</Translate>
              </h3>
              <p>
                <Translate contentKey="home.howItWorks.steps.step4.description">
                  Gestores acompanham resultados em relatórios claros e objetivos
                </Translate>
              </p>
            </div>
          </div>
          <div className="features-cards">
            <div className="feature-card">
              <h4>
                <Translate contentKey="home.howItWorks.features.students">
                  Para alunos: lições gamificadas, XP, ranking, conquistas
                </Translate>
              </h4>
            </div>
            <div className="feature-card">
              <h4>
                <Translate contentKey="home.howItWorks.features.teachers">
                  Para professores: criação de lições, acompanhamento da turma
                </Translate>
              </h4>
            </div>
            <div className="feature-card">
              <h4>
                <Translate contentKey="home.howItWorks.features.managers">Para gestores: métricas por escola, turma, município</Translate>
              </h4>
            </div>
          </div>
        </div>
      </section>

      {/* Platform Section */}
      <section className="section platform-section">
        <div className="section-container">
          <div className="section-header">
            <h2>
              <Translate contentKey="home.platform.title">O que a plataforma já faz hoje</Translate>
            </h2>
            <p>
              <Translate contentKey="home.platform.subtitle">MVP robusto pronto para implementação</Translate>
            </p>
          </div>
          <div className="platform-grid">
            <div className="platform-column">
              <h3>
                <Translate contentKey="home.platform.students.title">Para Alunos</Translate>
              </h3>
              <ul className="platform-features">
                <li>
                  <Translate contentKey="home.platform.students.features.gamification">
                    Sistema de XP, níveis e streak que engaja como em jogos
                  </Translate>
                </li>
                <li>
                  <Translate contentKey="home.platform.students.features.achievements">
                    Conquistas e ranking por município estimulam participação
                  </Translate>
                </li>
                <li>
                  <Translate contentKey="home.platform.students.features.content">
                    Conteúdos incluídos: vocabulário básico, gramática, listening
                  </Translate>
                </li>
              </ul>
            </div>
            <div className="platform-column">
              <h3>
                <Translate contentKey="home.platform.educators.title">Para Professores e Gestores</Translate>
              </h3>
              <ul className="platform-features">
                <li>
                  <Translate contentKey="home.platform.educators.features.reports">
                    Relatórios por turma, aluno e lição para decisões pedagógicas
                  </Translate>
                </li>
                <li>
                  <Translate contentKey="home.platform.educators.features.management">
                    Gestão completa de turmas e acompanhamento individual
                  </Translate>
                </li>
                <li>
                  <Translate contentKey="home.platform.educators.features.intervention">
                    Dados para intervenções pedagógicas direcionadas
                  </Translate>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </section>

      {/* Impact Section */}
      <section className="section">
        <div className="section-container">
          <div className="section-header">
            <h2>
              <Translate contentKey="home.impact.title">Impacto educacional esperado</Translate>
            </h2>
            <p>
              <Translate contentKey="home.impact.subtitle">Resultados mensuráveis para sua rede</Translate>
            </p>
          </div>
          <div className="impact-metrics">
            <div className="metric-card">
              <div className="metric-icon">
                <FontAwesomeIcon icon={faUsers} />
              </div>
              <h4>
                <Translate contentKey="home.impact.benefits.engagement">Melhora na frequência e engajamento</Translate>
              </h4>
              <p>
                <Translate contentKey="home.impact.benefits.engagement">Melhora na frequência e engajamento nas aulas de inglês</Translate>
              </p>
            </div>
            <div className="metric-card">
              <div className="metric-icon">
                <FontAwesomeIcon icon={faChartLine} />
              </div>
              <h4>
                <Translate contentKey="home.impact.benefits.tracking">Acompanhamento individualizado</Translate>
              </h4>
              <p>
                <Translate contentKey="home.impact.benefits.tracking">Acompanhamento individualizado mesmo em turmas grandes</Translate>
              </p>
            </div>
            <div className="metric-card">
              <div className="metric-icon">
                <FontAwesomeIcon icon={faGraduationCap} />
              </div>
              <h4>
                <Translate contentKey="home.impact.benefits.equity">Redução do abismo educacional</Translate>
              </h4>
              <p>
                <Translate contentKey="home.impact.benefits.equity">
                  Redução do abismo de acesso ao inglês entre ensino público e privado
                </Translate>
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Company Section */}
      <section className="section company-section">
        <div className="section-container">
          <div className="section-header">
            <h2>
              <Translate contentKey="home.company.title">Por que Fourground e Bumba Learning?</Translate>
            </h2>
            <p>
              <Translate contentKey="home.company.subtitle">Empresa brasileira focada em inclusão educacional</Translate>
            </p>
          </div>
          <div className="company-benefits">
            <div className="company-benefit">
              <p>
                <Translate contentKey="home.company.benefits.brazilian">
                  Empresa brasileira de educação e tecnologia focada em inclusão
                </Translate>
              </p>
            </div>
            <div className="company-benefit">
              <p>
                <Translate contentKey="home.company.benefits.scalable">
                  Solução escalável para redes completas, não só para uma escola
                </Translate>
              </p>
            </div>
            <div className="company-benefit">
              <p>
                <Translate contentKey="home.company.benefits.integration">
                  Integração simples aos processos já existentes da secretaria/escola
                </Translate>
              </p>
            </div>
          </div>
          <div className="manifesto">
            <p>
              <Translate contentKey="home.company.manifesto">
                Nossa visão é de um futuro bilíngue para estudantes da escola pública, conectada à simbologia do Bumba meu Boi como força
                cultural que une, celebra e ressignifica o território nordestino.
              </Translate>
            </p>
          </div>
        </div>
      </section>

      {/* Audience Section */}
      <section className="section audience-section">
        <div className="section-container">
          <div className="section-header">
            <h2>
              <Translate contentKey="home.audience.title">Para quem é?</Translate>
            </h2>
          </div>
          <div className="audience-cards">
            <div className="audience-card">
              <div className="audience-icon">
                <FontAwesomeIcon icon={faSchool} />
              </div>
              <h3>
                <Translate contentKey="home.audience.municipal.title">Secretarias municipais de educação</Translate>
              </h3>
              <p>
                <Translate contentKey="home.audience.municipal.description">
                  Visão macro, métricas por escola e implementação em rede
                </Translate>
              </p>
            </div>
            <div className="audience-card">
              <div className="audience-icon">
                <FontAwesomeIcon icon={faUserTie} />
              </div>
              <h3>
                <Translate contentKey="home.audience.state.title">Escolas estaduais e técnicas</Translate>
              </h3>
              <p>
                <Translate contentKey="home.audience.state.description">
                  Preparar estudantes para ENEM, vestibulares, mundo do trabalho
                </Translate>
              </p>
            </div>
            <div className="audience-card">
              <div className="audience-icon">
                <FontAwesomeIcon icon={faChalkboardTeacher} />
              </div>
              <h3>
                <Translate contentKey="home.audience.teachers.title">Professores de inglês da rede pública</Translate>
              </h3>
              <p>
                <Translate contentKey="home.audience.teachers.description">
                  Ferramentas de apoio, banco de questões, menos trabalho manual
                </Translate>
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* FAQ Section */}
      <section className="section">
        <div className="section-container">
          <div className="section-header">
            <h2>
              <Translate contentKey="home.faq.title">Perguntas frequentes</Translate>
            </h2>
          </div>
          <div className="faq-container">
            {[1, 2, 3, 4, 5, 6].map(index => (
              <div key={index} className={`faq-item ${openFaq === index ? 'active' : ''}`}>
                <button className="faq-question" onClick={() => toggleFaq(index)}>
                  <span>
                    <Translate contentKey={`home.faq.questions.q${index}.question`}>
                      {index === 1 && 'A plataforma funciona em computadores antigos?'}
                      {index === 2 && 'Quanto tempo leva para começar um piloto?'}
                      {index === 3 && 'Como os dados dos estudantes são protegidos?'}
                      {index === 4 && 'É possível usar com turmas grandes?'}
                      {index === 5 && 'O que os professores precisam saber para começar?'}
                      {index === 6 && 'A plataforma atende escolas com internet instável?'}
                    </Translate>
                  </span>
                  <FontAwesomeIcon icon={openFaq === index ? faMinus : faPlus} className="faq-icon" />
                </button>
                <div className={`faq-answer ${openFaq === index ? 'active' : ''}`}>
                  <p>
                    <Translate contentKey={`home.faq.questions.q${index}.answer`}>
                      {index === 1 &&
                        'Sim, o Bumba Learning foi desenvolvido para funcionar em equipamentos básicos comuns em escolas públicas.'}
                      {index === 2 &&
                        'Um piloto pode ser iniciado em até 2 semanas, incluindo formação dos professores e configuração das turmas.'}
                      {index === 3 && 'Seguimos rigorosamente a LGPD e todas as normas de proteção de dados educacionais.'}
                      {index === 4 && 'Sim, a plataforma suporta turmas de qualquer tamanho e oferece acompanhamento individualizado.'}
                      {index === 5 && 'Oferecemos formação completa. Professores precisam apenas de conhecimentos básicos de informática.'}
                      {index === 6 && 'Sim, as lições são otimizadas para funcionar com conexões limitadas e instáveis.'}
                    </Translate>
                  </p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Testimonials Section */}
      <section className="section testimonials-section">
        <div className="section-container">
          <div className="section-header">
            <h2>
              <Translate contentKey="home.testimonials.title">O que educadores estão dizendo</Translate>
            </h2>
          </div>
          <div className="testimonials-grid">
            <div className="testimonial-card">
              <p className="testimonial-text">
                <Translate contentKey="home.testimonials.testimonial1.text">
                  O engajamento dos alunos aumentou significativamente. Eles pedem para usar o Bumba Learning até no recreio!
                </Translate>
              </p>
              <p className="testimonial-author">
                <Translate contentKey="home.testimonials.testimonial1.author">
                  Coordenadora Pedagógica, Escola Municipal do Interior
                </Translate>
              </p>
            </div>
            <div className="testimonial-card">
              <p className="testimonial-text">
                <Translate contentKey="home.testimonials.testimonial2.text">
                  Finalmente conseguimos dados claros sobre o progresso individual de cada aluno. Isso mudou nossa forma de ensinar.
                </Translate>
              </p>
              <p className="testimonial-author">
                <Translate contentKey="home.testimonials.testimonial2.author">Professora de Inglês, Rede Municipal</Translate>
              </p>
            </div>
            <div className="testimonial-card">
              <p className="testimonial-text">
                <Translate contentKey="home.testimonials.testimonial3.text">
                  A implementação foi muito mais simples do que esperávamos. Em duas semanas já tínhamos resultados visíveis.
                </Translate>
              </p>
              <p className="testimonial-author">
                <Translate contentKey="home.testimonials.testimonial3.author">Gestora de Secretaria Municipal</Translate>
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Final CTA Section */}
      <section className="section final-cta-section">
        <div className="section-container">
          <div className="section-header">
            <h2>
              <Translate contentKey="home.finalCta.title">Leve inglês de qualidade para a sua rede pública</Translate>
            </h2>
            <p>
              <Translate contentKey="home.finalCta.subtitle">
                Uma solução inspirada no Bumba meu Boi, feita para a realidade das escolas do Nordeste
              </Translate>
            </p>
          </div>
          <form className="cta-form" onSubmit={handleContactSubmit}>
            <div className="form-grid">
              <div className="form-group">
                <label htmlFor="name">
                  <Translate contentKey="home.finalCta.form.name">Nome completo</Translate>
                </label>
                <input type="text" id="name" name="name" required />
              </div>
              <div className="form-group">
                <label htmlFor="position">
                  <Translate contentKey="home.finalCta.form.position">Cargo</Translate>
                </label>
                <select id="position" name="position" required>
                  <option value="">Selecione...</option>
                  <option value="secretary">
                    <Translate contentKey="home.finalCta.form.positions.secretary">Secretaria</Translate>
                  </option>
                  <option value="director">
                    <Translate contentKey="home.finalCta.form.positions.director">Direção</Translate>
                  </option>
                  <option value="coordinator">
                    <Translate contentKey="home.finalCta.form.positions.coordinator">Coordenação</Translate>
                  </option>
                  <option value="teacher">
                    <Translate contentKey="home.finalCta.form.positions.teacher">Docência</Translate>
                  </option>
                </select>
              </div>
              <div className="form-group">
                <label htmlFor="municipality">
                  <Translate contentKey="home.finalCta.form.municipality">Município/Estado</Translate>
                </label>
                <input type="text" id="municipality" name="municipality" required />
              </div>
              <div className="form-group">
                <label htmlFor="network">
                  <Translate contentKey="home.finalCta.form.network">Tipo de rede</Translate>
                </label>
                <select id="network" name="network" required>
                  <option value="">Selecione...</option>
                  <option value="municipal">
                    <Translate contentKey="home.finalCta.form.networks.municipal">Municipal</Translate>
                  </option>
                  <option value="state">
                    <Translate contentKey="home.finalCta.form.networks.state">Estadual</Translate>
                  </option>
                  <option value="federal">
                    <Translate contentKey="home.finalCta.form.networks.federal">Federal</Translate>
                  </option>
                </select>
              </div>
              <div className="form-group full-width">
                <label htmlFor="email">
                  <Translate contentKey="home.finalCta.form.email">E-mail institucional</Translate>
                </label>
                <input type="email" id="email" name="email" required />
              </div>
              <div className="form-group full-width">
                <label htmlFor="phone">
                  <Translate contentKey="home.finalCta.form.phone">Telefone/WhatsApp</Translate>
                </label>
                <input type="tel" id="phone" name="phone" />
              </div>
            </div>
            <div className="form-actions">
              <button type="submit" className="btn-white">
                <Translate contentKey="home.finalCta.cta.primary">Agendar uma conversa com a equipe</Translate>
              </button>
              <button type="button" className="btn-outline-white">
                <Translate contentKey="home.finalCta.cta.secondary">Receber material para apresentar à secretaria</Translate>
              </button>
            </div>
          </form>
        </div>
      </section>
    </div>
  );
};

export default Home;
