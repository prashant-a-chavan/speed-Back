import React from 'react';
import './AboutPage.css';

export const AboutPage: React.FC = () => {
  return (
    <div className="about-page-layout">
      <div className="about-card">
        <div className="about-header">
          <h2>About SpeedBack</h2>
        </div>

        <div className="about-section">
          <p>
            SpeedBack is a platform designed to simplify the process of scheduling feedback sessions
            within teams. It provides a clear, real-time overview of team members' availability and
            allows for quick and conflict-free bookings.
          </p>
        </div>

        <div className="about-section">
          <h3>How It Works</h3>
          <ul className="feature-list">
            <li>
              <strong>View Availability:</strong> The main dashboard provides a real-time,
              grid-based view of all team members and their availability across the three designated
              feedback slots.
            </li>
            <li>
              <strong>Book a Slot:</strong> Select your name, a time slot, and an available team
              member from the dropdowns to instantly book a session. The dashboard updates for
              everyone immediately.
            </li>
            <li>
              <strong>Clear & Conflict-Free:</strong> The system automatically prevents
              double-bookings. A team member cannot be booked if they are already a booker or a
              bookie in the same slot.
            </li>
            <li>
              <strong>Real-Time Updates:</strong> Powered by WebSockets, all bookings and
              cancellations are reflected on everyone's dashboard instantly, without needing to
              refresh the page.
            </li>
          </ul>
        </div>

        <div className="about-section">
          <h3>Technology Stack</h3>
          <p>This application is built using a modern, robust technology stack:</p>
          <div className="tech-stack-container">
            <div className="tech-column">
              <h4>Backend</h4>
              <ul>
                <li>Spring Boot</li>
                <li>JPA & Hibernate</li>
                <li>PostgreSQL Database</li>
                <li>Flyway for Database Migrations</li>
                <li>Spring WebSockets (STOMP)</li>
                <li>Feature Toggles</li>
              </ul>
            </div>
            <div className="tech-column">
              <h4>Frontend</h4>
              <ul>
                <li>React (with TypeScript)</li>
                <li>React Router for Navigation</li>
                <li>Axios for API Communication</li>
                <li>Prettier & ESLint for Code Quality</li>
                <li>Swagger for API Documentation</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
