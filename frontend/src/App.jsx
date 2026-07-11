import { useRef, useState } from 'react';

const examples = ["Let's circle back on this tomorrow.", "Do we have the bandwidth for this?", "Let's circle back tomorrow, touch base with design, and focus on the low-hanging fruit."];
export default function App() {
  const [text, setText] = useState(examples[0]); const [result, setResult] = useState(); const [loading, setLoading] = useState(false); const [error, setError] = useState(''); const [listening, setListening] = useState(false);
  const recognitionRef = useRef(null);
  const speechSupported = typeof window !== 'undefined' && ('SpeechRecognition' in window || 'webkitSpeechRecognition' in window);
  function startListening() {
    if (!speechSupported) { setError('Voice input is not supported in this browser. Please use a current Chrome or Edge browser.'); return; }
    const Recognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    const recognition = new Recognition(); recognition.lang = 'en-US'; recognition.continuous = false; recognition.interimResults = true;
    recognition.onstart = () => { setListening(true); setError(''); };
    recognition.onresult = event => setText(Array.from(event.results).map(result => result[0].transcript).join(' '));
    recognition.onerror = event => setError(`Voice recognition failed: ${event.error}.`);
    recognition.onend = () => setListening(false);
    recognitionRef.current = recognition; recognition.start();
  }
  function stopListening() { recognitionRef.current?.stop(); }
  async function translate(event) {
    event.preventDefault(); if (!text.trim()) return; setLoading(true); setError('');
    try { const response = await fetch('/api/v1/translations', { method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify({text}) }); if (!response.ok) throw new Error('Translation request failed.'); setResult(await response.json()); }
    catch (e) { setError(e.message + ' Is the backend running on port 8080?'); } finally { setLoading(false); }
  }
  return <main><section className="hero"><p className="eyebrow">WORKPLACE LANGUAGE, CLARIFIED</p><h1>Corporate jargon translator</h1><p>Paste a phrase, message, email, or use voice input to identify and explain workplace jargon.</p></section><section className="panel"><form onSubmit={translate}><label htmlFor="input">What did they say?</label><textarea id="input" value={text} onChange={e => setText(e.target.value)} maxLength="5000" placeholder="Paste or speak a phrase, message, or email excerpt…"/><div className="voice-row"><button type="button" className={listening ? 'voice listening' : 'voice'} onClick={listening ? stopListening : startListening} disabled={!speechSupported}>{listening ? 'Stop listening' : 'Use voice input'}</button><span>{speechSupported ? 'Your browser will ask for microphone permission.' : 'Voice input requires Chrome or Edge.'}</span></div><div className="actions"><button disabled={loading}>{loading ? 'Translating…' : 'Translate'}</button><span>{text.length}/5000</span></div></form><div className="examples">Try: {examples.map(example => <button key={example} onClick={() => setText(example)}>{example}</button>)}</div>{error && <p className="error">{error}</p>}</section>{result && <section className="result"><div className="meta"><span>{result.inputType.toLowerCase()}</span>{result.cached && <span>cached</span>}</div><article><h2>Plain meaning</h2><p>{result.plainMeaning}</p></article><article><h2>Professional explanation</h2><p>{result.professionalExplanation}</p></article><article className="reply"><h2>Suggested reply</h2><p>{result.suggestedReply}</p></article>{result.jargonTranslations?.length > 1 && <div className="translations"><h2>Detected jargon</h2>{result.jargonTranslations.map(item => <article className="translation" key={item.term}><h3>{item.term}</h3><p><strong>Meaning:</strong> {item.plainMeaning}</p><p><strong>Reply:</strong> {item.suggestedReply}</p>{item.dictionaryResults.length > 0 && <small>Source: {item.dictionaryResults.map(d => d.provider).join(', ')}</small>}</article>)}</div>}{result.dictionaryResults.length > 0 && <small>Dictionary source: {result.dictionaryResults.map(d => d.provider).join(', ')}</small>}</section>}</main>;
}