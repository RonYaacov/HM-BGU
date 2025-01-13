section .data
format db "%d: %s", 10, 0
args_msg db "Number of arguments: %d", 10, 0
debug_msg_string db "Debug: %s", 10, 0
debug_msg_int db "Debug: %d", 10, 0

section .text
global main
extern printf


main:

    push ebp
    mov ebp, esp
    mov eax, [ebp + 8] ; argc
    dec eax
    push eax
    push args_msg
    call printf ; print the number of arguments
    add esp, 8 ;clean the stack, the stack is now pointing to ebp
    ; print the arguments
    mov ecx, [ebp + 8] ; argc
    mov edx, [ebp + 12] ; argv
    add edx, 4 ; skip the first argument (the program name)
    mov eax, 1 ;start from the first argument that is not the program name
    
    .print_loop:
        cmp eax, ecx ;check if we reached the end of the arguments
        jge .end_print_loop ;if so, end the loop
        ;print the argument
        pusha 
        push dword [edx]
        push eax
        push format
        call printf
        add esp, 12 ;clean the stack
        popa
        add edx, 4 ;move to the next argument
        inc eax
        jmp .print_loop
        
    .end_print_loop:
    pop ebp
    ret
