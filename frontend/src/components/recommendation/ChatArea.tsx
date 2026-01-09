import { useEffect, useRef, useState } from 'react';
import { Button, Card, Form } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import ReactMarkdown from 'react-markdown';

import type { Message } from '../../pages/RecommendationsPage';
import sendSSERequest from '../../services/sseRequestHandler';
import generateThreadId from '../../utils/ThreadIDGenerator';
import style from './ChatArea.module.css';

const STORAGE_KEY_THREAD_ID = 'mmc-thread-id';
const STORAGE_KEY_MESSAGES = 'mmc-messages';
const STORAGE_KEY_CONVERSATION_COMPLETE = 'mmc-conversation-complete';

function ChatArea() {
    const [threadId, setThreadId] = useState<string | null>(() => localStorage.getItem(STORAGE_KEY_THREAD_ID));
    const [messages, setMessages] = useState<Message[]>(() => {
        try {
            const saved = localStorage.getItem(STORAGE_KEY_MESSAGES);
            return saved ? (JSON.parse(saved) as Message[]) : [];
        } catch (error) {
            console.error('Failed to parse messages', error);
            return [];
        }
    });
    const [conversationComplete, setConversationComplete] = useState<boolean>(() => {
        return localStorage.getItem(STORAGE_KEY_CONVERSATION_COMPLETE) === 'true';
    });
    const [inputValue, setInputValue] = useState('');
    const [isStreaming, setIsStreaming] = useState(false);
    const { t } = useTranslation();
    const inputRef = useRef<HTMLTextAreaElement>(null);
    const scrollableAreaRef = useRef<HTMLDivElement>(null);

    const sendMessage = () => {
        setMessages([...messages, { from: 'user', text: inputValue }]);
        setIsStreaming(true);
        sendSSERequest(setMessages, setIsStreaming, threadId!, inputValue, () => {
            setConversationComplete(true);
        })
            .then(() => {
                setIsStreaming(false);
            })
            .catch((error) => {
                console.error('Error sending SSE request:', error);
                setIsStreaming(false);
            });
    };

    const handleSend = () => {
        if (!inputValue.trim() || conversationComplete) return;
        sendMessage();
        setInputValue('');
        setTimeout(() => {
            inputRef.current?.focus();
        }, 0);
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            handleSend();
        }
    };

    // Hook that saves threadId to localStorage whenever it changes
    useEffect(() => {
        if (threadId) {
            localStorage.setItem(STORAGE_KEY_THREAD_ID, threadId);
        } else {
            localStorage.removeItem(STORAGE_KEY_THREAD_ID);
        }
    }, [threadId]);

    // Hook that saves messages to localStorage whenever they change
    useEffect(() => {
        localStorage.setItem(STORAGE_KEY_MESSAGES, JSON.stringify(messages));
    }, [messages]);

    // Hook that saves conversationComplete to localStorage whenever it changes
    useEffect(() => {
        localStorage.setItem(STORAGE_KEY_CONVERSATION_COMPLETE, String(conversationComplete));
    }, [conversationComplete]);

    useEffect(() => {
        if (scrollableAreaRef.current) {
            scrollableAreaRef.current.scrollTop = scrollableAreaRef.current.scrollHeight;
        }
    }, [messages]);

    const beginNewChat = () => {
        if (!threadId) {
            const newThreadId = generateThreadId();
            setThreadId(newThreadId);
            setMessages([]);
            setConversationComplete(false);
            setIsStreaming(true);
            sendSSERequest(setMessages, setIsStreaming, newThreadId, '', () => {
                setConversationComplete(true);
            })
                .then(() => {
                    setIsStreaming(false);
                })
                .catch((error) => {
                    console.error('Error sending SSE request:', error);
                    setIsStreaming(false);
                });
        }
    };

    if (!threadId) {
        return (
            <Card className="text-center">
                <Card.Body>
                    <Card.Title>{t('recommendations.title')}</Card.Title>
                    <Card.Text>{t('recommendations.startChat')}</Card.Text>
                    <Button variant="dark" onClick={beginNewChat}>
                        {t('recommendations.startChatButton')}
                    </Button>
                </Card.Body>
            </Card>
        );
    }

    return (
        <Card border="dark" className={style.mainCard}>
            <Card.Body>
                <Card.Title>{t('recommendations.title')}</Card.Title>
                <div className={style.chatContainer}>
                    <div className={style.topGradient} />
                    <div className={style.scrollableArea} ref={scrollableAreaRef}>
                        {messages.map((msg, index) => {
                            const isAgent = msg.from === 'agent';

                            const viewConfig = isAgent
                                ? {
                                    className: style.agentMessage,
                                    label: t('recommendations.agent'),
                                    content: <ReactMarkdown>{msg.text}</ReactMarkdown>,
                                }
                                : {
                                    className: style.userMessage,
                                    label: t('recommendations.user'),
                                    content: msg.text,
                                };

                            return (
                                <div key={`${msg.from}-${msg.text}-${index}`}>
                                    <Card.Text as="div" className={viewConfig.className}>
                                        <h6>{viewConfig.label}</h6>
                                        {viewConfig.content}
                                    </Card.Text>
                                    {index < messages.length - 1 ? <div className={style.separator} /> : null}
                                </div>
                            );
                        })}
                    </div>
                    <Card.Footer className={style.chatInputFooter}>
                        <div className="d-flex gap-2">
                            <Form.Control
                                className={style.chatInput}
                                ref={inputRef}
                                as="textarea"
                                rows={1}
                                placeholder={
                                    conversationComplete
                                        ? t('recommendations.conversationEnded')
                                        : t('recommendations.typeMessagePlaceholder')
                                }
                                value={inputValue}
                                onChange={(e) => setInputValue(e.target.value)}
                                onKeyDown={handleKeyDown}
                                disabled={isStreaming || conversationComplete}
                                aria-label="Chat input area to type message to agent"
                            />
                            <Button
                                onClick={handleSend}
                                disabled={isStreaming || conversationComplete}
                                className={style.sendButton}
                                aria-label={t('recommendations.sendMessageButton')}
                                aria-disabled={isStreaming || conversationComplete}
                            >
                                {t('recommendations.sendMessageButton')}
                            </Button>
                        </div>
                    </Card.Footer>
                </div>
            </Card.Body>
        </Card>
    );
}

export default ChatArea;
