import { useState } from "react";
import axios from "axios";
import "./App.css";

const API = "http://15.156.81.221:8080/api";

const initialForm = {
  name: "", industry: "", employeeCount: "", country: "",
  hasFirewall: false, hasAntiVirus: false, encryptsData: false,
  hasIncidentResponsePlan: false, hasSecurityTraining: false,
  previousBreaches: 0,
};

function RiskGauge({ score }) {
  const radius = 80;
  const circumference = Math.PI * radius;
  const progress = (score / 100) * circumference;
  const color = score >= 80 ? "#00ff9d" : score >= 60 ? "#ffd93d" : score >= 40 ? "#ff8c42" : "#ff4d6d";

  return (
    <div className="gauge-container">
      <svg width="200" height="120" viewBox="0 0 200 120">
        <path d="M 20 100 A 80 80 0 0 1 180 100" fill="none" stroke="#1a1a2e" strokeWidth="16" strokeLinecap="round"/>
        <path d="M 20 100 A 80 80 0 0 1 180 100" fill="none" stroke={color} strokeWidth="16"
          strokeLinecap="round" strokeDasharray={`${progress} ${circumference}`}
          style={{ transition: "stroke-dasharray 1s ease, stroke 0.5s ease" }}/>
        <text x="100" y="90" textAnchor="middle" fill={color} fontSize="32" fontFamily="'Courier New', monospace" fontWeight="bold">{score}</text>
        <text x="100" y="112" textAnchor="middle" fill="#666" fontSize="11" fontFamily="'Courier New', monospace">RISK SCORE</text>
      </svg>
    </div>
  );
}

function RiskBadge({ score }) {
  if (score >= 80) return <span className="badge low">LOW RISK</span>;
  if (score >= 60) return <span className="badge medium">MEDIUM RISK</span>;
  if (score >= 40) return <span className="badge high">HIGH RISK</span>;
  return <span className="badge critical">CRITICAL RISK</span>;
}

export default function App() {
  const [form, setForm] = useState(initialForm);
  const [company, setCompany] = useState(null);
  const [memo, setMemo] = useState("");
  const [loading, setLoading] = useState(false);
  const [memoLoading, setMemoLoading] = useState(false);
  const [step, setStep] = useState("form");

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm(prev => ({ ...prev, [name]: type === "checkbox" ? checked : value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const res = await axios.post(`${API}/companies`, {
        ...form, employeeCount: parseInt(form.employeeCount), previousBreaches: parseInt(form.previousBreaches)
      });
      setCompany(res.data);
      setStep("result");
    } catch (err) {
      alert("Error submitting assessment");
    } finally {
      setLoading(false);
    }
  };

  const handleGenerateMemo = async () => {
    setMemoLoading(true);
    setMemo("");
    try {
      const res = await axios.post(`${API}/companies/${company.id}/generate-memo`);
      setMemo(res.data);
    } catch (err) {
      alert("Error generating memo");
    } finally {
      setMemoLoading(false);
    }
  };

  const reset = () => { setForm(initialForm); setCompany(null); setMemo(""); setStep("form"); };

  const checkboxFields = [
    { name: "hasFirewall", label: "Firewall" },
    { name: "hasAntiVirus", label: "Antivirus Software" },
    { name: "encryptsData", label: "Data Encryption" },
    { name: "hasIncidentResponsePlan", label: "Incident Response Plan" },
    { name: "hasSecurityTraining", label: "Security Training Program" },
  ];

  return (
    <div className="app">
      <header>
        <div className="header-inner">
          <div className="logo">
            <span className="logo-bracket">[</span>
            CYBERISK
            <span className="logo-bracket">]</span>
          </div>
          <div className="header-sub">Automated Cyber Insurance Underwriting Platform</div>
        </div>
      </header>

      <main>
        {step === "form" && (
          <div className="card animate-in">
            <div className="card-header">
              <span className="card-tag">NEW ASSESSMENT</span>
              <h2>Company Security Profile</h2>
              <p>Submit a company's security posture for automated risk analysis</p>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="form-grid">
                <div className="field">
                  <label>Company Name</label>
                  <input name="name" value={form.name} onChange={handleChange} placeholder="Acme Corp" required/>
                </div>
                <div className="field">
                  <label>Industry</label>
                  <select name="industry" value={form.industry} onChange={handleChange} required>
                    <option value="">Select industry</option>
                    {["Finance","Healthcare","Technology","Retail","Manufacturing","Legal","Education","Government"].map(i => <option key={i}>{i}</option>)}
                  </select>
                </div>
                <div className="field">
                  <label>Employee Count</label>
                  <input name="employeeCount" type="number" value={form.employeeCount} onChange={handleChange} placeholder="500" required/>
                </div>
                <div className="field">
                  <label>Country</label>
                  <input name="country" value={form.country} onChange={handleChange} placeholder="Canada" required/>
                </div>
                <div className="field">
                  <label>Previous Breaches</label>
                  <input name="previousBreaches" type="number" min="0" value={form.previousBreaches} onChange={handleChange}/>
                </div>
              </div>

              <div className="security-section">
                <div className="section-label">SECURITY CONTROLS</div>
                <div className="checkbox-grid">
                  {checkboxFields.map(({ name, label }) => (
                    <label key={name} className={`checkbox-card ${form[name] ? "checked" : ""}`}>
                      <input type="checkbox" name={name} checked={form[name]} onChange={handleChange}/>
                      <span className="check-indicator">{form[name] ? "✓" : "○"}</span>
                      <span>{label}</span>
                    </label>
                  ))}
                </div>
              </div>

              <button type="submit" className="btn-primary" disabled={loading}>
                {loading ? <span className="loading-text">ANALYZING<span className="dots">...</span></span> : "RUN RISK ASSESSMENT →"}
              </button>
            </form>
          </div>
        )}

        {step === "result" && company && (
          <div className="animate-in">
            <div className="result-header">
              <button className="btn-back" onClick={reset}>← New Assessment</button>
              <div className="company-name">{company.name}</div>
              <div className="company-meta">{company.industry} · {company.country} · {company.employeeCount} employees</div>
            </div>

            <div className="result-grid">
              <div className="card score-card">
                <div className="card-tag">RISK SCORE</div>
                <RiskGauge score={company.riskScore}/>
                <RiskBadge score={company.riskScore}/>
                <div className="score-breakdown">
                  {[
                    { label: "Firewall", val: company.hasFirewall },
                    { label: "Antivirus", val: company.hasAntiVirus },
                    { label: "Encryption", val: company.encryptsData },
                    { label: "IR Plan", val: company.hasIncidentResponsePlan },
                    { label: "Training", val: company.hasSecurityTraining },
                  ].map(({ label, val }) => (
                    <div key={label} className="breakdown-row">
                      <span>{label}</span>
                      <span className={val ? "status-good" : "status-bad"}>{val ? "✓" : "✗"}</span>
                    </div>
                  ))}
                  <div className="breakdown-row">
                    <span>Prior Breaches</span>
                    <span className={company.previousBreaches === 0 ? "status-good" : "status-bad"}>{company.previousBreaches}</span>
                  </div>
                </div>
              </div>

              <div className="card memo-card">
                <div className="card-tag">UNDERWRITING MEMO</div>
                <h3>AI-Generated Assessment</h3>
                <p className="memo-sub">Generate a professional underwriting memo based on the risk analysis</p>

                {!memo && (
                  <button className="btn-primary" onClick={handleGenerateMemo} disabled={memoLoading}>
                    {memoLoading ? <span className="loading-text">GENERATING<span className="dots">...</span></span> : "GENERATE MEMO →"}
                  </button>
                )}

                {memo && (
                  <div className="memo-content">
                    {memo.split("\n").map((line, i) => (
                      <p key={i}>{line}</p>
                    ))}
                  </div>
                )}
              </div>
            </div>
          </div>
        )}
      </main>
    </div>
  );
}