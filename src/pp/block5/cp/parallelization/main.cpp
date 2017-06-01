#include <stdio.h>
#include <tests.h>
#include <kernels.h>

int main(int argc, char* argv[]) {
    (void)argc;
    (void)argv;

    FILE* out = stdout;

    fprintf(out, "Testing matrix multiply (sequential)...\n");
    test_multiply(out, impl_matrix_multiply_sequential);

    fprintf(out, "Testing matrix multiply (openmp)...\n");
    test_multiply(out, impl_matrix_multiply_openmp);

    fprintf(out, "\n");

    fprintf(out, "Testing count zero (sequential)...\n");
    test_countzero(out, impl_matrix_countzero_sequential);

    fprintf(out, "Testing count zero (openmp)...\n");
    test_countzero(out, impl_matrix_countzero_openmp);

    fprintf(out, "Testing count zero (opencl)...\n");
    test_countzero(out, impl_matrix_countzero_opencl);

    fprintf(out, "\n");

    fprintf(out, "Testing vector sort (sequential)...\n");
    test_vectorsort(out, impl_vector_sort_sequential);

    fprintf(out, "Testing vector sort (openmp)...\n");
    test_vectorsort(out, impl_vector_sort_openmp);

    return 0;
}
