import type { Message } from '../pages/RecommendationsPage';

type StreamEvent =
    | { type: 'token'; content: string; thread_id: string }
    | { type: 'error'; content: string; thread_id: string }
    | { type: 'done'; thread_id: string }
    | { type: 'recommendation_complete'; thread_id: string };

const appendToLastAgentMessage = (setMessages: React.Dispatch<React.SetStateAction<Message[]>>, text: string) => {
    setMessages((prev) => {
        const lastMessage = prev[prev.length - 1];
        if (lastMessage?.from === 'agent') {
            return [...prev.slice(0, -1), { ...lastMessage, text: lastMessage.text + text }];
        }
        return prev;
    });
};

async function sendSSERequest(
    setMessages: React.Dispatch<React.SetStateAction<Message[]>>,
    setIsStreaming: React.Dispatch<React.SetStateAction<boolean>>,
    threadId: string,
    message: string,
    onConversationComplete?: () => void,
): Promise<void> {
    const agentUrl: string = import.meta.env.VITE_AGENT_URL as string;
    if (!agentUrl) {
        throw new Error('Agent URL is not defined in environment variables');
    }

    // Add placeholder agent message before streaming
    setMessages((prev) => [...prev, { from: 'agent', text: '' }]);

    try {
        const payload = {
            thread_id: threadId,
            ...(message !== null && { message }),
        };

        const response = await fetch(`${agentUrl}/chat`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload),
        });

        if (!response.body) {
            throw new Error('No response body');
        }

        const reader = response.body.getReader();
        const decoder = new TextDecoder();
        let buffer = '';

        while (true) {
            const { done, value } = await reader.read();
            if (done) break;

            const chunk = decoder.decode(value, { stream: true });
            buffer += chunk;

            const lines = buffer.split('\n');
            buffer = lines.pop() ?? '';

            for (const line of lines) {
                if (line.startsWith('data: ')) {
                    const jsonStr = line.slice(6);
                    try {
                        const data = JSON.parse(jsonStr) as StreamEvent;

                        if (data.type === 'token') {
                            appendToLastAgentMessage(setMessages, data.content);
                        } else if (data.type === 'error') {
                            appendToLastAgentMessage(setMessages, `\n[Error: ${data.content}]`);
                        } else if (data.type === 'done') {
                            setIsStreaming(false);
                        } else if (data.type === 'recommendation_complete') {
                            setIsStreaming(false);
                            onConversationComplete?.();
                        }
                    } catch (e) {
                        console.error('Error parsing SSE data:', e);
                    }
                }
            }
        }
    } catch (error) {
        console.error('Fetch error:', error);
        appendToLastAgentMessage(setMessages, `\n[Connection Error]`);
        setIsStreaming(false);
    }
}

export default sendSSERequest;
