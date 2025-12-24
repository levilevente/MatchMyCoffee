class ThinkBlockFilter:
    """Filters out <think>...</think> blocks from streamed content."""

    def __init__(self):
        self.is_thinking = False
        self.thinking_node: str | None = None

    def reset_if_node_changed(self, node_name: str) -> None:
        """Reset thinking state if we switched to a different node."""
        if self.is_thinking and self.thinking_node != node_name:
            self.is_thinking = False
            self.thinking_node = None

    def process(self, content: str, node_name: str) -> tuple[list[str], bool]:
        """
        Process content and filter out think blocks.

        Returns:
            tuple: (list of content chunks to emit, whether to send 'thinking' status)
        """
        self.reset_if_node_changed(node_name)
        chunks_to_emit = []
        send_thinking_status = False

        if "<think>" in content:
            self.is_thinking = True
            self.thinking_node = node_name
            pre_think, remainder = content.split("<think>", 1)

            if pre_think:
                chunks_to_emit.append(pre_think)

            if "</think>" in remainder:
                self.is_thinking = False
                self.thinking_node = None
                post_think = remainder.split("</think>", 1)[-1]
                if post_think:
                    chunks_to_emit.append(post_think)
            else:
                send_thinking_status = True

        elif "</think>" in content:
            self.is_thinking = False
            self.thinking_node = None
            post_think = content.split("</think>", 1)[-1]
            if post_think:
                chunks_to_emit.append(post_think)

        elif not self.is_thinking:
            chunks_to_emit.append(content)

        return chunks_to_emit, send_thinking_status
