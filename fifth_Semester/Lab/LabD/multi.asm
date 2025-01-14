section .data
format_byte db "%02hhx", 10, 0
format_word db "%04hx", 0
end_of_line db 10, 0
debug_msg_string db "Debug: %s", 10, 0
debug_msg_int db "Debug: %d", 10, 0
x_struct dw 5
x_num dw 0x00aa, 1,2,0x0044,0x004f

section .text
global main
extern printf


main:
    pusha
    mov eax, [x_struct]
    and eax, 0xFFFF ; Zero-extend the word to dword
    push x_num
    push eax
    call print_multi 
    add esp, 8 ;clean the stack
    popa
    ret

print_multi:
    push ebp
    mov ebp, esp   
    pusha
    mov eax, [ebp + 8] ; x_struct
    mov ebx, [ebp + 8 + 4 ] ; address of x_num
    .print_loop:
        cmp eax, 0 ;check if we reached the end of the struct
        je .end_print_loop ;if so, end the loop
        ;print the argument
        mov esi, [ebx + 2*eax - 2]
        pusha
        push esi
        push format_word
        call printf
        add esp, 8 ;clean the stack
        popa
        dec eax ;decrement the argument index
        jmp .print_loop
    
    .end_print_loop:
    push end_of_line
    call printf
    add esp, 4 ;clean the stack for end_of_line
    popa
    pop ebp
    ret